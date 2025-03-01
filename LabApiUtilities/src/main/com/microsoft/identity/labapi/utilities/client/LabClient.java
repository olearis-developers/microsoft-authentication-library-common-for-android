// Copyright (c) Microsoft Corporation.
// All rights reserved.
//
// This code is licensed under the MIT License.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files(the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions :
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
package com.microsoft.identity.labapi.utilities.client;

import com.microsoft.identity.internal.test.labapi.ApiException;
import com.microsoft.identity.internal.test.labapi.Configuration;
import com.microsoft.identity.internal.test.labapi.api.ConfigApi;
import com.microsoft.identity.internal.test.labapi.api.CreateTempUserApi;
import com.microsoft.identity.internal.test.labapi.api.DeleteDeviceApi;
import com.microsoft.identity.internal.test.labapi.api.DisablePolicyApi;
import com.microsoft.identity.internal.test.labapi.api.EnablePolicyApi;
import com.microsoft.identity.internal.test.labapi.api.LabSecretApi;
import com.microsoft.identity.internal.test.labapi.api.ResetApi;
import com.microsoft.identity.internal.test.labapi.model.ConfigInfo;
import com.microsoft.identity.internal.test.labapi.model.CustomSuccessResponse;
import com.microsoft.identity.internal.test.labapi.model.SecretResponse;
import com.microsoft.identity.internal.test.labapi.model.TempUser;
import com.microsoft.identity.internal.test.labapi.model.UserInfo;
import com.microsoft.identity.labapi.utilities.authentication.LabApiAuthenticationClient;
import com.microsoft.identity.labapi.utilities.constants.ProtectionPolicy;
import com.microsoft.identity.labapi.utilities.constants.TempUserType;
import com.microsoft.identity.labapi.utilities.constants.ResetOperation;
import com.microsoft.identity.labapi.utilities.constants.UserType;
import com.microsoft.identity.labapi.utilities.exception.LabApiException;
import com.microsoft.identity.labapi.utilities.exception.LabError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LabClient implements ILabClient {

    private final LabApiAuthenticationClient mLabApiAuthenticationClient;
    private final long PASSWORD_RESET_WAIT_DURATION = TimeUnit.MINUTES.toMillis(1);
    private final long LAB_API_RETRY_WAIT = TimeUnit.SECONDS.toMillis(5);
    private final long TEMP_USER_CREATION_WAIT = TimeUnit.SECONDS.toMillis(30);

    /**
     * Temp users API provided by Lab team can often take more than 10 seconds to return...hence, we
     * are overriding the read timeout.
     */
    private static final int TEMP_USER_API_READ_TIMEOUT = (int) TimeUnit.SECONDS.toMillis(15);

    @Override
    public ILabAccount getLabAccount(@NonNull final LabQuery labQuery) throws LabApiException {
        final List<ConfigInfo> configInfos = fetchConfigsFromLab(labQuery);
        // for each query, lab actually returns a list of accounts..all of which fit the criteria..
        // usually we only need one such account, and hence over here we are just picking the first
        // element of the list.
        final ConfigInfo configInfo = configInfos.get(0);
        return getLabAccountObject(configInfo);
    }

    @Override
    public List<ILabAccount> getLabAccounts(@NonNull final LabQuery labQuery) throws LabApiException {
        final List<ConfigInfo> configInfos = fetchConfigsFromLab(labQuery);

        final List<ILabAccount> labAccounts = new ArrayList<>(configInfos.size());

        for (final ConfigInfo configInfo : configInfos) {
            labAccounts.add(getLabAccountObject(configInfo));
        }

        return labAccounts;
    }

    private ILabAccount getLabAccountObject(@NonNull final ConfigInfo configInfo) throws LabApiException {
        // for guest accounts the UPN is located under homeUpn field
        String username = configInfo.getUserInfo().getHomeUPN();
        if (username == null || username.equals("") || username.equalsIgnoreCase("None")) {
            // for accounts that are NOT guest..the UPN is directly on the UPN field
            username = configInfo.getUserInfo().getUpn();
        }

        final String password = getPassword(configInfo);

        return new LabAccount.LabAccountBuilder()
                .username(username)
                .password(password)
                .userType(UserType.fromName(configInfo.getUserInfo().getUserType()))
                .homeTenantId(configInfo.getUserInfo().getHomeTenantID())
                .configInfo(configInfo)
                .build();
    }

    private List<ConfigInfo> fetchConfigsFromLab(@NonNull final LabQuery query) throws LabApiException {
        Configuration.getDefaultApiClient().setAccessToken(
                mLabApiAuthenticationClient.getAccessToken()
        );
        try {
            final ConfigApi api = new ConfigApi();
            return api.apiConfigGet(
                    valueOf(query.getUserType()),
                    valueOf(query.getUserRole()),
                    valueOf(query.getMfa()),
                    valueOf(query.getProtectionPolicy()),
                    valueOf(query.getHomeDomain()),
                    valueOf(query.getHomeUpn()),
                    valueOf(query.getB2cProvider()),
                    valueOf(query.getFederationProvider()),
                    valueOf(query.getAzureEnvironment()),
                    valueOf(query.getGuestHomeAzureEnvironment()),
                    valueOf(query.getAppType()),
                    valueOf(query.getPublicClient()),
                    valueOf(query.getSignInAudience()),
                    valueOf(query.getGuestHomedIn()),
                    valueOf(query.getHasAltId()),
                    valueOf(query.getAltIdSource()),
                    valueOf(query.getAltIdType()),
                    valueOf(query.getPasswordPolicyValidityPeriod()),
                    valueOf(query.getPasswordPolicyNotificationDays()),
                    valueOf(query.getTokenLifetimePolicy()),
                    valueOf(query.getTokenType()),
                    valueOf(query.getTokenLifetime())
            );
        } catch (final com.microsoft.identity.internal.test.labapi.ApiException ex) {
            throw new LabApiException(LabError.FAILED_TO_GET_ACCOUNT_FROM_LAB, ex);
        }
    }

    private String valueOf(final Object obj) {
        return obj == null ? null : obj.toString();
    }

    @Override
    public ILabAccount createTempAccount(@NonNull final TempUserType tempUserType) throws LabApiException {
        // Adding a second attempt here, api sometimes fails to create the temp user.
        try {
            return createTempAccountInternal(tempUserType);
        } catch (final LabApiException e){
            if (LabError.FAILED_TO_CREATE_TEMP_USER.equals(e.getErrorCode())){

                // Wait for a bit
                try {
                    Thread.sleep(LAB_API_RETRY_WAIT);
                } catch (final InterruptedException e2) {
                    e2.printStackTrace();
                }

                // Try to create the temp account again
                return createTempAccountInternal(tempUserType);
            } else {
                throw e;
            }
        }
    }

    private ILabAccount createTempAccountInternal(@NonNull final TempUserType tempUserType) throws LabApiException {
        Configuration.getDefaultApiClient().setAccessToken(
                mLabApiAuthenticationClient.getAccessToken()
        );
        final CreateTempUserApi createTempUserApi = new CreateTempUserApi();
        createTempUserApi.getApiClient().setReadTimeout(TEMP_USER_API_READ_TIMEOUT);
        final TempUser tempUser;

        try {
            tempUser = createTempUserApi.apiCreateTempUserPost(valueOf(tempUserType));
        } catch (final ApiException e) {
            throw new LabApiException(LabError.FAILED_TO_CREATE_TEMP_USER, e);
        }

        final String password = getPassword(tempUser);

        // Adding a wait to finish temp user creation
        try {
            Thread.sleep(TEMP_USER_CREATION_WAIT);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

        return new LabAccount.LabAccountBuilder()
                .username(tempUser.getUpn())
                .password(password)
                // all temp users created by Lab Api are currently cloud users
                .userType(UserType.CLOUD)
                .homeTenantId(tempUser.getTenantId())
                .build();
    }

    @Override
    public LabGuestAccount loadGuestAccountFromLab(LabQuery labQuery) throws LabApiException {
        final List<ConfigInfo> configInfoList = fetchConfigsFromLab(labQuery);

        List<String> guestLabTenants = new ArrayList<>();
        for (ConfigInfo configInfo : configInfoList) {
            guestLabTenants.add(configInfo.getUserInfo().getTenantID());
        }

        // pick one config info object to obtain home tenant information
        // doesn't matter which one as all have the same home tenant
        final ConfigInfo configInfo = configInfoList.get(0);
        final UserInfo userInfo = configInfo.getUserInfo();

        return new LabGuestAccount(
                userInfo.getHomeUPN(),
                userInfo.getHomeDomain(),
                userInfo.getHomeTenantID(),
                guestLabTenants
        );
    }

    @Override
    public String getPasswordForGuestUser(LabGuestAccount guestUser) throws LabApiException {
        final String labName = guestUser.getHomeDomain().split("\\.")[0];

        // Adding a second attempt here, api sometimes fails to get the lab secret.
        try {
            return getSecret(labName);
        } catch (final LabApiException e){
            if (e.getErrorCode().equals(LabError.FAILED_TO_GET_SECRET_FROM_LAB)){

                // Wait for a bit
                try {
                    Thread.sleep(LAB_API_RETRY_WAIT);
                } catch (final InterruptedException e2) {
                    e2.printStackTrace();
                }

                // Try to get the secret again
                return getSecret(labName);
            } else {
                throw e;
            }
        }
    }

    @Override
    public String getSecret(@NonNull final String secretName) throws LabApiException {
        Configuration.getDefaultApiClient().setAccessToken(
                mLabApiAuthenticationClient.getAccessToken()
        );
        final LabSecretApi labSecretApi = new LabSecretApi();

        try {
            final SecretResponse secretResponse = labSecretApi.apiLabSecretGet(secretName);
            return secretResponse.getValue();
        } catch (final com.microsoft.identity.internal.test.labapi.ApiException ex) {
            throw new LabApiException(LabError.FAILED_TO_GET_SECRET_FROM_LAB, ex);
        }
    }

    @Override
    public boolean deleteDevice(@NonNull final String upn,
                                @NonNull final String deviceId) throws LabApiException {
        Configuration.getDefaultApiClient().setAccessToken(
                mLabApiAuthenticationClient.getAccessToken()
        );
        final DeleteDeviceApi deleteDeviceApi = new DeleteDeviceApi();

        try {
            final CustomSuccessResponse successResponse = deleteDeviceApi.apiDeleteDeviceDelete(
                    upn, deviceId
            );

            // we probably need a more sophisticated logger integrated into LabApi
            // for now this is fine
            System.out.println(successResponse.getResult());

            final String expectedResult = String.format(
                    "Device : %s, successfully deleted from AAD.", deviceId
            );
            return expectedResult.equalsIgnoreCase(successResponse.getResult());
        } catch (final com.microsoft.identity.internal.test.labapi.ApiException ex) {
            throw new LabApiException(
                    LabError.FAILED_TO_DELETE_DEVICE, ex,
                    ex.getResponseBody() != null ? ex.getResponseBody() : "Response body missing from Exception"
            );
        }
    }

    @Override
    public boolean deleteDevice(@NonNull final String upn,
                                @NonNull final String deviceId,
                                final int numDeleteAttempts,
                                final long waitTimeBeforeEachDeleteAttempt) throws LabApiException {
        for (int i = 0; i < numDeleteAttempts; i++) {
            System.out.printf(Locale.ENGLISH, "Delete device attempt #%d%n", (i + 1));
            // Lab may not find the device right away so we try every 2 seconds
            // we do 5 attempts, if that doesn't work then we fail
            try {
                Thread.sleep(waitTimeBeforeEachDeleteAttempt);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }

            try {
                if (deleteDevice(upn, deviceId)) {
                    return true;
                }
            } catch (final LabApiException labApiException) {
                // if not the last attempt, then just print the error to console
                if (i < (numDeleteAttempts - 1)) {
                    System.out.printf(
                            Locale.ENGLISH,
                            "Delete device attempt #%d%n failed: %s", (i + 1),
                            labApiException
                    );
                } else {
                    // last attempt, just throw the exception back
                    throw labApiException;
                }
            }

        }

        // there was no error, but device still not deleted
        return false;
    }

    private String getPassword(@NonNull final ConfigInfo configInfo) throws LabApiException {
        return getPassword(configInfo.getLabInfo().getCredentialVaultKeyName());
    }

    private String getPassword(@NonNull final TempUser tempUser) throws LabApiException {
        return getPassword(tempUser.getCredentialVaultKeyName());
    }

    private String getPassword(final String credentialVaultKeyName) throws LabApiException {
        final String secretName = getLabSecretName(credentialVaultKeyName);

        // Adding a second attempt here, api sometimes fails to get the lab secret.
        try {
            return getSecret(secretName);
        } catch (final LabApiException e){
            if (e.getErrorCode().equals(LabError.FAILED_TO_GET_SECRET_FROM_LAB)){

                // Wait for a bit
                try {
                    Thread.sleep(LAB_API_RETRY_WAIT);
                } catch (final InterruptedException e2) {
                    e2.printStackTrace();
                }

                // Try to get the secret again
                return getSecret(secretName);
            } else {
                throw e;
            }
        }
    }

    @Override
    public boolean resetPassword(@NonNull final String upn) throws LabApiException {
        final ResetApi resetApi = new ResetApi();
        try {
            final CustomSuccessResponse resetResponse = resetApi.apiResetPut(upn, ResetOperation.PASSWORD.toString());
            final String expectedResult = ("Password reset successful for user : " + upn)
                    .toLowerCase();
            final boolean result = resetResponse.getResult().toLowerCase().contains(expectedResult);
            if (result) {
                try {
                    Thread.sleep(PASSWORD_RESET_WAIT_DURATION);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return result;
        } catch (final ApiException e) {
            throw new LabApiException(LabError.FAILED_TO_RESET_PASSWORD, e);
        }
    }

    @Override
    public boolean resetPassword(@NonNull final String upn,
                                 final int resetAttempts) throws LabApiException {
        for (int i = 0; i < resetAttempts; i++) {
            System.out.printf(Locale.ENGLISH, "Password reset attempt #%d%n", (i + 1));

            try {
                if (resetPassword(upn)) {
                    return true;
                }
            } catch (final LabApiException labApiException) {
                // if not the last attempt, then just print the error to console
                if (i < (resetAttempts - 1)) {
                    System.out.printf(
                            Locale.ENGLISH,
                            "Password reset attempt #%d%n failed: %s", (i + 1),
                            labApiException
                    );

                    try {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    // last attempt, just throw the exception back
                    throw labApiException;
                }
            }
        }

        // there was no error, but password was not reset
        return false;
    }

    private String getLabSecretName(final String credentialVaultKeyName) {
        final String[] parts = credentialVaultKeyName.split("/");
        return parts[parts.length - 1];
    }

    /**
     * Enable CA/Special Policies for any Locked User.
     * Enable Policy can be used for GlobalMFA, MAMCA, MDMCA, MFAONSPO, MFAONEXO.   Also test users can have more than 1 policy assigned to the same user.
     *
     * @param upn    Enter a valid Locked User UPN (optional)
     * @param policy Enable Policy can be used for GlobalMFA, MAMCA, MDMCA, MFAONSPO, MFAONEXO. (optional)
     * @return boolean value indicating policy enabled or not.
     */
    public boolean enablePolicy(@NonNull final String upn, @NonNull final ProtectionPolicy policy) {
        final EnablePolicyApi enablePolicyApi = new EnablePolicyApi();
        try {
            final CustomSuccessResponse customSuccessResponse = enablePolicyApi.apiEnablePolicyPut(upn, policy.toString());
            final String expectedResult = (policy + " Enabled for user : " + upn).toLowerCase();
            final String result = customSuccessResponse.getResult();
            if (result != null) {
                return result.toLowerCase().contains(expectedResult);
            }
            return false;
        } catch (final ApiException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Disable CA/Special Policies for any Locked User.
     * Disable Policy can be used for GlobalMFA, MAMCA, MDMCA, MFAONSPO, MFAONEXO.   Also test users can have more than 1 policy assigned to the same user.
     *
     * @param upn    Enter a valid Locked User UPN (optional)
     * @param policy Disable Policy can be used for GlobalMFA, MAMCA, MDMCA, MFAONSPO, MFAONEXO. (optional)
     * @return boolean value indicating policy is disabled or not for the upn.
     */
    public boolean disablePolicy(@NonNull final String upn, @NonNull final ProtectionPolicy policy) {
        final DisablePolicyApi disablePolicyApi = new DisablePolicyApi();
        try {
            final CustomSuccessResponse customSuccessResponse = disablePolicyApi.apiDisablePolicyPut(upn, policy.toString());
            final String expectedResult = (policy + " Disabled for user : " + upn).toLowerCase();
            final String result = customSuccessResponse.getResult();
            if (result != null) {
                return result.toLowerCase().contains(expectedResult);
            }
            return false;
        } catch (final ApiException e) {
            throw new AssertionError(e);
        }
    }
}
