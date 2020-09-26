//  Copyright (c) Microsoft Corporation.
//  All rights reserved.
//
//  This code is licensed under the MIT License.
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files(the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions :
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.
package com.microsoft.identity.common.internal.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Pair;

import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.microsoft.identity.common.WarningType;
import com.microsoft.identity.common.exception.BaseException;
import com.microsoft.identity.common.exception.IntuneAppProtectionPolicyRequiredException;
import com.microsoft.identity.common.exception.UserCancelException;
import com.microsoft.identity.common.internal.commands.BaseCommand;
import com.microsoft.identity.common.internal.commands.InteractiveTokenCommand;
import com.microsoft.identity.common.internal.commands.parameters.BrokerInteractiveTokenCommandParameters;
import com.microsoft.identity.common.internal.commands.parameters.InteractiveTokenCommandParameters;
import com.microsoft.identity.common.internal.commands.parameters.SilentTokenCommandParameters;
import com.microsoft.identity.common.internal.eststelemetry.EstsTelemetry;
import com.microsoft.identity.common.internal.logging.DiagnosticContext;
import com.microsoft.identity.common.internal.logging.Logger;
import com.microsoft.identity.common.internal.result.AcquireTokenResult;
import com.microsoft.identity.common.internal.result.LocalAuthenticationResult;
import com.microsoft.identity.common.internal.result.ResultFuture;
import com.microsoft.identity.common.internal.telemetry.Telemetry;
import com.microsoft.identity.common.internal.util.BiConsumer;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.microsoft.identity.common.adal.internal.AuthenticationConstants.AuthorizationIntentAction.CANCEL_INTERACTIVE_REQUEST;
import static com.microsoft.identity.common.adal.internal.AuthenticationConstants.AuthorizationIntentAction.RETURN_INTERACTIVE_REQUEST_RESULT;
import static com.microsoft.identity.common.adal.internal.AuthenticationConstants.AuthorizationIntentKey.REQUEST_CODE;
import static com.microsoft.identity.common.adal.internal.AuthenticationConstants.AuthorizationIntentKey.RESULT_CODE;

public class CommandDispatcher {

    private static final String TAG = CommandDispatcher.class.getSimpleName();

    private static final int SILENT_REQUEST_THREAD_POOL_SIZE = 5;
    private static final ExecutorService sInteractiveExecutor = Executors.newSingleThreadExecutor();
    private static final ExecutorService sSilentExecutor = Executors.newFixedThreadPool(SILENT_REQUEST_THREAD_POOL_SIZE);
    private static final Object sLock = new Object();
    private static InteractiveTokenCommand sCommand = null;
    private static final CommandResultCache sCommandResultCache = new CommandResultCache();

    private static final Object mapAccessLock = new Object();
    @GuardedBy("mapAccessLock")
    // Suppressing rawtype warnings due to the generic type BaseCommand
    @SuppressWarnings(WarningType.rawtype_warning)
    private static ConcurrentMap<BaseCommand, ResultFuture<CommandResult>> sExecutingCommandMap = new ConcurrentHashMap<>();

    /**
     * Remove all keys that are the command reference from the executing command map.  Since if they key has
     * been changed, remove will not work, construct a new map and add all keys that are not identically
     * that key into the new map.
     * @param command the command whose identity to use to cleanse the map.
     */
    // Suppressing rawtype warnings due to the generic type BaseCommand
    @SuppressWarnings(WarningType.rawtype_warning)
    private static void cleanMap(BaseCommand command) {
        ConcurrentMap<BaseCommand, ResultFuture<CommandResult>> newMap = new ConcurrentHashMap<>();
        for (Map.Entry<BaseCommand, ResultFuture<CommandResult>> e : sExecutingCommandMap.entrySet()) {
            if (command != e.getKey()) {
                newMap.put(e.getKey(), e.getValue());
            }
        }
        sExecutingCommandMap = newMap;
    }

    /**
     * submitSilent - Run a command using the silent thread pool.
     *
     * @param command
     */
    public static void submitSilent(@SuppressWarnings(WarningType.rawtype_warning) @NonNull final BaseCommand command) {
        final String methodName = ":submitSilent";
        Logger.verbose(
                TAG + methodName,
                "Beginning execution of silent command."
        );

        final Handler handler = new Handler(Looper.getMainLooper());
        synchronized (mapAccessLock) {
            ResultFuture<CommandResult> future = sExecutingCommandMap.get(command);

            if (null == future) {
                future = new ResultFuture<>();
                final ResultFuture<CommandResult> putValue = sExecutingCommandMap.putIfAbsent(command, future);

                if (null == putValue) {
                    // our value was inserted.
                    future.whenComplete(getCommandResultConsumer(command, handler));
                } else {
                    // Our value was not inserted, grab the one that was and hang a new listener off it
                    putValue.whenComplete(getCommandResultConsumer(command, handler));
                    return;
                }
            } else {
                future.whenComplete(getCommandResultConsumer(command, handler));
                return;
            }

            final ResultFuture<CommandResult> finalFuture = future;

            sSilentExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String correlationId = initializeDiagnosticContext(command.getParameters().getCorrelationId());

                        // set correlation id on parameters as it may not already be set
                        command.getParameters().setCorrelationId(correlationId);

                        EstsTelemetry.getInstance().initTelemetryForCommand(command);

                        EstsTelemetry.getInstance().emitApiId(command.getPublicApiId());

                        CommandResult commandResult = null;

                        //Log operation parameters
                        if (command.getParameters() instanceof SilentTokenCommandParameters) {
                            logSilentRequestParams(methodName, (SilentTokenCommandParameters) command.getParameters());
                            EstsTelemetry.getInstance().emitForceRefresh(((SilentTokenCommandParameters) command.getParameters()).isForceRefresh());
                        }

                        //Check cache to see if the same command completed in the last 30 seconds
                        commandResult = sCommandResultCache.get(command);

                        //If nothing in cache, execute the command and cache the result
                        if (commandResult == null) {
                            commandResult = executeCommand(command);
                            cacheCommandResult(command, commandResult);
                        } else {
                            Logger.info(
                                    TAG + methodName,
                                    "Silent command result returned from cache."
                            );
                        }

                        // set correlation id on Local Authentication Result
                        setCorrelationIdOnResult(commandResult, correlationId);

                        Telemetry.getInstance().flush(correlationId);
                        EstsTelemetry.getInstance().flush(command, commandResult);
                        finalFuture.setResult(commandResult);
                        //Return the result via the callback
                    } catch (final Throwable t) {
                        finalFuture.setException(new ExecutionException(t));
                    } finally {
                        synchronized (mapAccessLock) {
                            final ResultFuture mapFuture = sExecutingCommandMap.remove(command);
                            if (mapFuture == null) {
                                // If this has happened, the command that we started with has mutated.  We will
                                // examine every entry in the map, find the one with the same object identity
                                // and remove it.
                                // ADO:TODO:1153495 - Rekey this map with stable string keys.
                                Logger.error(TAG, "The command in the map has mutated " + command.getClass().getCanonicalName()
                                        + " the calling application was " + command.getParameters().getApplicationName(), null);
                                cleanMap(command);
                            }
                        }
                    }
                }
            });
        }
    }

    private static BiConsumer<CommandResult, Throwable> getCommandResultConsumer(
            @SuppressWarnings(WarningType.rawtype_warning) @NonNull final BaseCommand command,
            @NonNull final Handler handler) {
        return new BiConsumer<CommandResult, Throwable>() {
            @Override
            public void accept(CommandResult result, final Throwable throwable) {
                if (null != throwable) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            commandCallBackOnError(command, throwable);
                        }
                    });
                    return;
                }

                // Return command result will post() result for us.
                returnCommandResult(command, result, handler);
            }
        };
    }

    // Suppressing unchecked warnings due to casting of Throwable to the generic type of TaskCompletedCallbackWithError
    @SuppressWarnings(WarningType.unchecked_warning)
    private static void commandCallBackOnError(@SuppressWarnings(WarningType.rawtype_warning) @NonNull BaseCommand command, Throwable throwable) {
        command.getCallback().onError(throwable);
    }

    static void clearCommandCache() {
        sCommandResultCache.clear();
    }

    /**
     * We need to inspect the AcquireTokenResult type to determine whether the request was successful, cancelled or encountered an exception
     * <p>
     * Execute the command provided to the command dispatcher
     *
     * @param command
     * @return
     */
    private static CommandResult executeCommand(@SuppressWarnings(WarningType.rawtype_warning) BaseCommand command) {

        Object result = null;
        BaseException baseException = null;
        CommandResult commandResult;

        try {
            //Try executing request
            result = command.execute();
        } catch (final Exception e) {
            if (e instanceof BaseException) {
                baseException = (BaseException) e;
            } else {
                baseException = ExceptionAdapter.baseExceptionFromException(e);
            }
        }

        if (baseException != null) {
            if (baseException instanceof UserCancelException) {
                commandResult = new CommandResult(CommandResult.ResultStatus.CANCEL, null);
            } else {
                //Post On Error
                commandResult = new CommandResult(CommandResult.ResultStatus.ERROR, baseException);
            }
        } else {
            if (result != null && result instanceof AcquireTokenResult) {
                //Handler handler, final BaseCommand command, BaseException baseException, AcquireTokenResult result
                commandResult = getCommandResultFromTokenResult(baseException, (AcquireTokenResult) result);
            } else {
                //For commands that don't return an AcquireTokenResult
                commandResult = new CommandResult(CommandResult.ResultStatus.COMPLETED, result);
            }
        }

        return commandResult;

    }

    /**
     * Return the result of the command to the caller via the callback associated with the command
     *
     * @param command
     * @param result
     * @param handler
     */
    private static void returnCommandResult(@SuppressWarnings(WarningType.rawtype_warning) final BaseCommand command, final CommandResult result, Handler handler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                switch (result.getStatus()) {
                    case ERROR:
                        commandCallbackOnError(command, result);
                        break;
                    case COMPLETED:
                        commandCallbackOnTaskCompleted(command, result);
                        break;
                    case CANCEL:
                        command.getCallback().onCancel();
                        break;
                    default:

                }
            }
        });
    }

    // Suppressing unchecked warnings due to casting of the result to the generic type of TaskCompletedCallbackWithError
    @SuppressWarnings(WarningType.unchecked_warning)
    private static void commandCallbackOnError(@SuppressWarnings("rawtypes") BaseCommand command, CommandResult result) {
        command.getCallback().onError(result.getResult());
    }

    // Suppressing unchecked warnings due to casting of the result to the generic type of TaskCompletedCallback
    @SuppressWarnings(WarningType.unchecked_warning)
    private static void commandCallbackOnTaskCompleted(@SuppressWarnings("rawtypes") BaseCommand command, CommandResult result) {
        command.getCallback().onTaskCompleted(result.getResult());
    }

    /**
     * Cache the result of the command (if eligible to do so) in order to protect the service from clients
     * making the requests in a tight loop
     *
     * @param command
     * @param commandResult
     */
    private static void cacheCommandResult(@SuppressWarnings(WarningType.rawtype_warning) BaseCommand command, CommandResult commandResult) {
        if (command.isEligibleForCaching() && eligibleToCache(commandResult)) {
            sCommandResultCache.put(command, commandResult);
        }
    }

    /**
     * Determine if the command result should be cached
     *
     * @param commandResult
     * @return
     */
    private static boolean eligibleToCache(CommandResult commandResult) {
        switch (commandResult.getStatus()) {
            case ERROR:
                return eligibleToCacheException((BaseException) commandResult.getResult());
            case COMPLETED:
                return true;
            default:
                return false;
        }
    }

    /**
     * Determine if the exception type is eligible to be cached
     *
     * @param exception
     * @return
     */
    private static boolean eligibleToCacheException(BaseException exception) {
        if (exception instanceof IntuneAppProtectionPolicyRequiredException) {
            return false;
        }
        return true;
    }


    /**
     * Get Commandresult from acquiretokenresult
     *
     * @param baseException
     * @param result
     */
    private static CommandResult getCommandResultFromTokenResult(BaseException baseException, AcquireTokenResult result) {
        //Token Commands
        if (result.getSucceeded()) {
            return new CommandResult(CommandResult.ResultStatus.COMPLETED, result.getLocalAuthenticationResult());
        } else {
            //Get MsalException from Authorization and/or Token Error Response
            baseException = ExceptionAdapter.exceptionFromAcquireTokenResult(result);
            if (baseException instanceof UserCancelException) {
                return new CommandResult(CommandResult.ResultStatus.CANCEL, null);
            } else {
                return new CommandResult(CommandResult.ResultStatus.ERROR, baseException);
            }
        }
    }

    public static void beginInteractive(final InteractiveTokenCommand command) {
        final String methodName = ":beginInteractive";
        Logger.info(
                TAG + methodName,
                "Beginning interactive request"
        );
        synchronized (sLock) {
            final LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(command.getParameters().getAndroidApplicationContext());

            // only send broadcast to cancel if within broker
            if (command.getParameters() instanceof BrokerInteractiveTokenCommandParameters) {
                // Send a broadcast to cancel if any active auth request is present.
                localBroadcastManager.sendBroadcast(
                        new Intent(CANCEL_INTERACTIVE_REQUEST)
                );
            }

            sInteractiveExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    final String correlationId = initializeDiagnosticContext(
                            command.getParameters().getCorrelationId()
                    );

                    // set correlation id on parameters as it may not already be set
                    command.getParameters().setCorrelationId(correlationId);

                    EstsTelemetry.getInstance().initTelemetryForCommand(command);

                    EstsTelemetry.getInstance().emitApiId(command.getPublicApiId());

                    if (command.getParameters() instanceof InteractiveTokenCommandParameters) {
                        logInteractiveRequestParameters(methodName, (InteractiveTokenCommandParameters) command.getParameters());
                    }

                    final BroadcastReceiver resultReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            completeInteractive(intent);
                        }
                    };

                    CommandResult commandResult;
                    Handler handler = new Handler(Looper.getMainLooper());

                    localBroadcastManager.registerReceiver(
                            resultReceiver,
                            new IntentFilter(RETURN_INTERACTIVE_REQUEST_RESULT));

                    sCommand = command;

                    //Try executing request
                    commandResult = executeCommand(command);
                    sCommand = null;
                    localBroadcastManager.unregisterReceiver(resultReceiver);

                    // set correlation id on Local Authentication Result
                    setCorrelationIdOnResult(commandResult, correlationId);

                    EstsTelemetry.getInstance().flush(command, commandResult);
                    Telemetry.getInstance().flush(correlationId);
                    returnCommandResult(command, commandResult, handler);
                }
            });
        }
    }

    private static void logInteractiveRequestParameters(final String methodName,
                                                        final InteractiveTokenCommandParameters params) {
        Logger.info(
                TAG + methodName,
                "Requested "
                        + params.getScopes().size()
                        + " scopes"
        );

        Logger.infoPII(
                TAG + methodName,
                "----\nRequested scopes:"
        );
        for (final String scope : params.getScopes()) {
            Logger.infoPII(
                    TAG + methodName,
                    "\t" + scope
            );
        }
        Logger.infoPII(
                TAG + methodName,
                "----"
        );
        Logger.infoPII(
                TAG + methodName,
                "ClientId: [" + params.getClientId() + "]"
        );
        Logger.infoPII(
                TAG + methodName,
                "RedirectUri: [" + params.getRedirectUri() + "]"
        );
        Logger.infoPII(
                TAG + methodName,
                "Login hint: [" + params.getLoginHint() + "]"
        );

        if (null != params.getExtraQueryStringParameters()) {
            Logger.infoPII(
                    TAG + methodName,
                    "Extra query params:"
            );
            for (final Pair<String, String> qp : params.getExtraQueryStringParameters()) {
                Logger.infoPII(
                        TAG + methodName,
                        "\t\"" + qp.first + "\":\"" + qp.second + "\""
                );
            }
        }

        if (null != params.getExtraScopesToConsent()) {
            Logger.infoPII(
                    TAG + methodName,
                    "Extra scopes to consent:"
            );
            for (final String extraScope : params.getExtraScopesToConsent()) {
                Logger.infoPII(
                        TAG + methodName,
                        "\t" + extraScope
                );
            }
        }

        Logger.info(
                TAG + methodName,
                "Using authorization agent: " + params.getAuthorizationAgent().toString()
        );

        if (null != params.getAccount()) {
            Logger.infoPII(
                    TAG + methodName,
                    "Using account: " + params.getAccount().getHomeAccountId()
            );
        }
    }

    private static void logSilentRequestParams(final String methodName,
                                               final SilentTokenCommandParameters parameters) {
        Logger.infoPII(
                TAG + methodName,
                "ClientId: [" + parameters.getClientId() + "]"
        );
        Logger.infoPII(
                TAG + methodName,
                "----\nRequested scopes:"
        );

        for (final String scope : parameters.getScopes()) {
            Logger.infoPII(
                    TAG + methodName,
                    "\t" + scope
            );
        }
        Logger.infoPII(
                TAG + methodName,
                "----"
        );

        if (null != parameters.getAccount()) {
            Logger.infoPII(
                    TAG + methodName,
                    "Using account: " + parameters.getAccount().getHomeAccountId()
            );
        }

        Logger.info(
                TAG + methodName,
                "Force refresh? [" + parameters.isForceRefresh() + "]"
        );
    }

    private static void completeInteractive(final Intent resultIntent) {
        final String methodName = ":completeInteractive";

        int requestCode = resultIntent.getIntExtra(REQUEST_CODE, 0);
        int resultCode = resultIntent.getIntExtra(RESULT_CODE, 0);

        if (sCommand != null) {
            sCommand.notify(requestCode, resultCode, resultIntent);
        } else {
            Logger.warn(TAG + methodName, "sCommand is null, No interactive call in progress to complete.");
        }
    }

    public static String initializeDiagnosticContext(@Nullable final String requestCorrelationId) {
        final String methodName = ":initializeDiagnosticContext";

        final String correlationId = TextUtils.isEmpty(requestCorrelationId) ?
                UUID.randomUUID().toString() :
                requestCorrelationId;

        final com.microsoft.identity.common.internal.logging.RequestContext rc =
                new com.microsoft.identity.common.internal.logging.RequestContext();
        rc.put(DiagnosticContext.CORRELATION_ID, correlationId);
        DiagnosticContext.setRequestContext(rc);
        Logger.verbose(
                TAG + methodName,
                "Initialized new DiagnosticContext"
        );

        return correlationId;
    }

    public static int getCachedResultCount() {
        return sCommandResultCache.getSize();
    }

    private static void setCorrelationIdOnResult(@NonNull final CommandResult commandResult,
                                                 @NonNull final String correlationId) {
        // set correlation id on Local Authentication Result
        if (commandResult.getResult() != null &&
                commandResult.getResult() instanceof LocalAuthenticationResult) {
            final LocalAuthenticationResult localAuthenticationResult =
                    (LocalAuthenticationResult) commandResult.getResult();
            localAuthenticationResult.setCorrelationId(correlationId);
        }
    }

}
