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
package com.microsoft.identity.common.adal.internal;

import androidx.annotation.VisibleForTesting;

import com.microsoft.identity.common.internal.logging.Logger;

import java.nio.charset.Charset;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * {@link AuthenticationConstants} contains all the constant value the SDK is using.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthenticationConstants {
    /**
     * The logging tag for this class.
     */
    private static final String TAG =  AuthenticationConstants.class.getSimpleName();

    /**
     * ADAL package name.
     */
    public static final String ADAL_PACKAGE_NAME = "com.microsoft.aad.adal";

    /**
     * Microsoft apps family of client Id.
     */
    public static final String MS_FAMILY_ID = "1";

    /**
     * The Constant ENCODING_UTF8.
     */
    public static final String ENCODING_UTF8 = "UTF-8";

    /**
     * The Constant CHARSET_UTF8.
     */
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    /**
     * The Constant CHARSET_ASCII.
     */
    public static final Charset CHARSET_ASCII = Charset.forName("ASCII");

    /**
     * Bundle message.
     */
    public static final String BUNDLE_MESSAGE = "Message";

    /**
     * Default access token expiration time in seconds.
     */
    public static final int DEFAULT_EXPIRATION_TIME_SEC = 3600;

    /**
     * The constant label for SP800-108.
     */
    public static final String SP800_108_LABEL = "AzureAD-SecureConversation";

    /**
     * A constant for PMD to be happy with.
     */
    public static final String ONE_POINT_ZERO = "1.0";
    public static final String TWO_POINT_ZERO = "2.0";


    /**
     * Holding all the constant value involved in the webview.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Browser {

        /**
         * Represents the request object used to construct request sent to authorize endpoint.
         */
        public static final String REQUEST_MESSAGE = "com.microsoft.aad.adal:BrowserRequestMessage";

        /**
         * Represents the request object returned from webview.
         */
        public static final String RESPONSE_REQUEST_INFO = "com.microsoft.aad.adal:BrowserRequestInfo";

        /**
         * Represents the error code returned from webview.
         */
        public static final String RESPONSE_ERROR_CODE = "com.microsoft.aad.adal:BrowserErrorCode";

        /**
         * Represents the error subcode returned from webview.
         */
        public static final String RESPONSE_ERROR_SUBCODE = "com.microsoft.aad.adal:BrowserErrorSubCode";

        /**
         * Represents the error message returned from webview.
         */
        public static final String RESPONSE_ERROR_MESSAGE = "com.microsoft.aad.adal:BrowserErrorMessage";

        /**
         * Represents the exception returned from webview.
         */
        public static final String RESPONSE_AUTHENTICATION_EXCEPTION = "com.microsoft.aad.adal:AuthenticationException";

        /**
         * Represents the final url that webview receives.
         */
        public static final String RESPONSE_FINAL_URL = "com.microsoft.aad.adal:BrowserFinalUrl";

        /**
         * Represents the response returned from broker.
         */
        public static final String RESPONSE = "com.microsoft.aad.adal:BrokerResponse";

        /**
         * Represent the error code of invalid request returned from webview.
         */
        public static final String WEBVIEW_INVALID_REQUEST = "Invalid request";

        /**
         * Used by LocalBroadcastReceivers to filter the intent string of request cancellation.
         */
        public static final String ACTION_CANCEL = "com.microsoft.aad.adal:BrowserCancel";

        /**
         * Used as the key to send back request id.
         */
        public static final String REQUEST_ID = "com.microsoft.aad.adal:RequestId";

        /**
         * Sub error returned by server representing the user cancel the auth flow.
         */
        public static final String SUB_ERROR_UI_CANCEL = "cancel";

        /**
         * V2 endpoint for logging the user out in browser.
         */
        public static final String LOGOUT_ENDPOINT_V2 = "https://login.microsoftonline.com/common/oauth2/v2.0/logout";

        /**
         * Go-link URL for documentation on troubleshooting common SSL, ADFS issues.
         */
        public static final String SSL_HELP_URL = "https://go.microsoft.com/fwlink/?linkid=2138180";
    }

    /**
     * Represents the response code.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UIResponse {

        /**
         * Represents that user cancelled the flow.
         */
        public static final int BROWSER_CODE_CANCEL = 2001;

        /**
         * Represents that browser error is returned.
         */
        public static final int BROWSER_CODE_ERROR = 2002;

        /**
         * Represents that the authorization code is returned successfully.
         */
        public static final int BROWSER_CODE_COMPLETE = 2003;

        /**
         * Represents that broker successfully returns the response.
         */
        public static final int TOKEN_BROKER_RESPONSE = 2004;

        /**
         * Webview throws Authentication exception. It needs to be send to callback.
         */
        public static final int BROWSER_CODE_AUTHENTICATION_EXCEPTION = 2005;

        /**
         * CA flow, device doesn't have company portal or azure authenticator installed.
         * Waiting for broker package to be installed, and resume request in broker.
         */
        public static final int BROKER_REQUEST_RESUME = 2006;

        /**
         * Device registration in broker apps.
         */
        public static final int BROWSER_CODE_DEVICE_REGISTER = 2007;

        /**
         * Represents that SDK signalled to cancelled the auth flow as app
         * launched a new interactive auth request
         */
        public static final int BROWSER_CODE_SDK_CANCEL = 2008;

        /**
         * MDM Flow is triggered.
         */
        public static final int BROWSER_CODE_MDM = 2009;
    }

    /**
     * Represents the request code.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UIRequest {

        /**
         * Represents the request of browser flow.
         */
        public static final int BROWSER_FLOW = 1001;

        /**
         * Represents the request of token flow.
         */
        public static final int TOKEN_FLOW = 1002;

        /**
         * Represents the request of broker flow.
         */
        public static final int BROKER_FLOW = 1003;
    }

    /**
     * Represents the constant value of oauth2 params.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class OAuth2 {

        /**
         * String of access token.
         */
        public static final String ACCESS_TOKEN = "access_token";

        /**
         * String of authority.
         */
        public static final String AUTHORITY = "authority";

        /**
         * String of authorization code.
         */
        public static final String AUTHORIZATION_CODE = "authorization_code";

        /**
         * String of client id.
         */
        public static final String CLIENT_ID = "client_id";

        /**
         * String of client secret.
         */
        public static final String CLIENT_SECRET = "client_secret";

        /**
         * String of client info.
         */
        public static final String CLIENT_INFO = "client_info";

        /**
         * String value used to indicate client_info is requested from the token endpoint.
         */
        public static final String CLIENT_INFO_TRUE = "1";

        /**
         * String of AAD version.
         */
        public static final String AAD_VERSION = "ver";

        /**
         * Constant for  v1 endpoint
         */
        public static final String AAD_VERSION_V1 = ONE_POINT_ZERO;

        /**
         * Constsnt for v2 endpoint
         */
        public static final String AAD_VERSION_V2 = TWO_POINT_ZERO;

        /**
         * String of preferred user name.
         */
        public static final String AAD_PREFERRED_USERNAME = "preferred_username";

        /**
         * String of code.
         */
        public static final String CODE = "code";

        /**
         * String of error.
         */
        public static final String ERROR = "error";

        /**
         * String of suberror.
         */
        public static final String SUBERROR = "suberror";

        /**
         * String of error description.
         */
        public static final String ERROR_DESCRIPTION = "error_description";

        /**
         * String of error codes.
         */
        public static final String ERROR_CODES = "error_codes";

        /**
         * String of expires in.
         */
        public static final String EXPIRES_IN = "expires_in";

        /**
         * String of grant type.
         */
        public static final String GRANT_TYPE = "grant_type";

        /**
         * String redirect uri.
         */
        public static final String REDIRECT_URI = "redirect_uri";

        /**
         * String of refresh token.
         */
        public static final String REFRESH_TOKEN = "refresh_token";

        /**
         * String of response type.
         */
        public static final String RESPONSE_TYPE = "response_type";

        /**
         * String of scope.
         */
        public static final String SCOPE = "scope";

        /**
         * String of state.
         */
        public static final String STATE = "state";

        /**
         * String of token type.
         */
        public static final String TOKEN_TYPE = "token_type";

        /**
         * String of http web response body.
         */
        public static final String HTTP_RESPONSE_BODY = "response_body";

        /**
         * String of http web response headers.
         */
        public static final String HTTP_RESPONSE_HEADER = "response_headers";

        /**
         * String of http web response status code.
         */
        public static final String HTTP_STATUS_CODE = "status_code";

        /**
         * String of id token.
         */
        public static final String ID_TOKEN = "id_token";

        /**
         * String of sub in the id token.
         */
        public static final String ID_TOKEN_SUBJECT = "sub";

        /**
         * String of tenant id in the id token.
         */
        public static final String ID_TOKEN_TENANTID = "tid";

        /**
         * String of UPN in the id token claim.
         */
        public static final String ID_TOKEN_UPN = "upn";

        /**
         * String of given name in the id token claim.
         */
        public static final String ID_TOKEN_GIVEN_NAME = "given_name";

        /**
         * String of family name in the id token claim.
         */
        public static final String ID_TOKEN_FAMILY_NAME = "family_name";

        /**
         * String of unique name.
         */
        public static final String ID_TOKEN_UNIQUE_NAME = "unique_name";

        /**
         * String of email in the id token.
         */
        public static final String ID_TOKEN_EMAIL = "email";

        /**
         * String of identity provider in the id token claim.
         */
        public static final String ID_TOKEN_IDENTITY_PROVIDER = "idp";

        /**
         * String of oid in the id token claim.
         */
        public static final String ID_TOKEN_OBJECT_ID = "oid";

        /**
         * String of password expiration in the id token claim.
         */
        public static final String ID_TOKEN_PASSWORD_EXPIRATION = "pwd_exp";

        /**
         * String of password change url in the id token claim.
         */
        public static final String ID_TOKEN_PASSWORD_CHANGE_URL = "pwd_url";

        /**
         * String of FoCI field returned in the JSON response from token endpoint.
         */
        public static final String ADAL_CLIENT_FAMILY_ID = "foci";

        /**
         * String of has_chrome sent as extra query param to hide back button in the webview.
         */
        public static final String HAS_CHROME = "haschrome";

        /**
         * String for extended expiration time.
         */
        public static final String EXT_EXPIRES_IN = "ext_expires_in";

        /**
         * String for claims.
         */
        public static final String CLAIMS = "claims";

        /**
         * String as JSON key to send client capabilities.
         */
        public static final String CLIENT_CAPABILITIES_CLAIMS_LIST = "xms_cc";

        /**
         * String as JSON key to send access token claims.
         */
        public static final String CLIENT_CAPABILITY_ACCESS_TOKEN = "access_token";

        /**
         * String for cloud instance host name.
         */
        public static final String CLOUD_INSTANCE_HOST_NAME = "cloud_instance_host_name";
        /**
         * session key JWE.
         */
        public static final String SESSION_KEY_JWE = "session_key_jwe";

        /**
         * String as Query parameter key to send a V1 request to V2 endpoint
         */
        public static final String IT_VER_PARAM = "itver";
    }

    /**
     * Represents the constants value for Active Directory.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class AAD {

        /**
         * AAD OAuth2 extension strings.
         */
        public static final String RESOURCE = "resource";

        /**
         * AAD OAuth2 Challenge strings.
         */
        public static final String BEARER = "Bearer";

        /**
         * AAD Oauth2 authorization.
         */
        public static final String AUTHORIZATION = "authorization";

        /**
         * String of authorization uri.
         */
        public static final String AUTHORIZATION_URI = "authorization_uri";

        /**
         * AAD Oauth2 string of realm.
         */
        public static final String REALM = "realm";

        /**
         * String of login hint.
         */
        public static final String LOGIN_HINT = "login_hint";

        /**
         * String of access denied.
         */
        public static final String WEB_UI_CANCEL = "access_denied";

        /**
         * String of correlation id.
         */
        public static final String CORRELATION_ID = "correlation_id";

        /**
         * String of client request id.
         */
        public static final String CLIENT_REQUEST_ID = "client-request-id";

        /**
         * String of return client request id.
         */
        public static final String RETURN_CLIENT_REQUEST_ID = "return-client-request-id";

        /**
         * String of prompt.
         */
        public static final String QUERY_PROMPT = "prompt";

        /**
         * String of prompt behavior as always.
         */
        public static final String QUERY_PROMPT_VALUE = "login";

        /**
         * String of prompt behavior as refresh session.
         */
        public static final String QUERY_PROMPT_REFRESH_SESSION_VALUE = "refresh_session";

        /**
         * String of ADAL platform.
         */
        public static final String ADAL_ID_PLATFORM = "x-client-SKU";

        /**
         * String of ADAL version.
         */
        public static final String ADAL_ID_VERSION = "x-client-Ver";

        /**
         * String of ADAL id CPU.
         */
        public static final String ADAL_ID_CPU = "x-client-CPU";

        /**
         * String of client app os version.
         */
        public static final String ADAL_ID_OS_VER = "x-client-OS";

        /**
         * String of ADAL ID DM.
         */
        public static final String ADAL_ID_DM = "x-client-DM";

        /**
         * String of platform value for the sdk.
         */
        public static final String ADAL_ID_PLATFORM_VALUE = "Android";

        /**
         * String for request id returned from Evo.
         **/
        public static final String REQUEST_ID_HEADER = "x-ms-request-id";

        /**
         * String for the broker version.
         */
        public static final String ADAL_BROKER_VERSION = "x-client-brkrver";

        /**
         * String for the host app name
         */
        public static final String APP_PACKAGE_NAME = "x-app-name";

        /**
         * String for the host app version
         */
        public static final String APP_VERSION = "x-app-ver";
    }

    /**
     * Represents the constants for broker.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Broker {

        /**
         * Broker feature with multi-user.
         */
        public static final String BROKER_FEATURE_MULTI_USER = "broker.feature.multi.user";

        /**
         * Broker request id.
         */
        public static final int BROKER_REQUEST_ID = 1177;

        /**
         * String for broker request.
         */
        public static final String BROKER_REQUEST = "com.microsoft.aadbroker.adal.broker.request";

        /**
         * String for broker request resume.
         */
        public static final String BROKER_REQUEST_RESUME = "com.microsoft.aadbroker.adal.broker.request.resume";

        /**
         * String for broker return JSON.
         */
        public static final String BROKER_RETURN_JSON = "broker.json";

        /**
         * Account type string.
         */
        public static final String BROKER_ACCOUNT_TYPE = "com.microsoft.workaccount";

        /**
         * String of account initial name.
         */
        public static final String ACCOUNT_INITIAL_NAME = "aad";

        /**
         * String of background request message.
         */
        public static final String BACKGROUND_REQUEST_MESSAGE = "background.request";

        /**
         * String of account default.
         */
        public static final String ACCOUNT_DEFAULT_NAME = "Default";

        /**
         * String of broker version.
         */
        public static final String BROKER_VERSION = "broker.version";

        /**
         * String of broker package name.
         */
        public static final String BROKER_PACKAGE_NAME = "broker.package.name";

        /**
         * String of broker AccountChooserActivity name.
         */
        public static final String BROKER_ACTIVITY_NAME = "broker.activity.name";

        /**
         * The Msal-To-Broker protocol name.
         */
        public static final String MSAL_TO_BROKER_PROTOCOL_NAME = "msal.to.broker";

        /**
         * The newest Msal-To-Broker protocol version.
         */
        public static final String MSAL_TO_BROKER_PROTOCOL_VERSION_CODE = computeMaxMsalBrokerProtocol();

        @VisibleForTesting
        public static String computeMaxMsalBrokerProtocol() {
            String stringVersion = BrokerContentProvider.BROKER_VERSION_1;
            float protocolVersion = 1.0f;
            for (final BrokerContentProvider.API api : BrokerContentProvider.API.values()) {
                final String version = api.getMsalVersion();
                if (version != null) {
                    try {
                        final float candVersion = Float.parseFloat(version);
                        if (candVersion > protocolVersion) {
                            protocolVersion = candVersion;
                            stringVersion = version;
                        }
                    } catch (final NumberFormatException nfe) {
                        Logger.error(TAG + ":<static initialization>", "Invalid value for protocol version", nfe);
                    }
                }
            }
            return stringVersion;
        }

        /**
         * The BrokerAPI-To-Broker protocol name.
         */
        public static final String BROKER_API_TO_BROKER_PROTOCOL_NAME = "broker.api.to.broker";

        /**
         * The newest BrokerAPI-To-Broker protocol version.
         */
        public static final String BROKER_API_TO_BROKER_PROTOCOL_VERSION_CODE = computeMaxHostBrokerProtocol();

        @VisibleForTesting
        public static String computeMaxHostBrokerProtocol() {
            String stringVersion = BrokerContentProvider.VERSION_1;
            float protocolVersion = 1.0f;
            for (final BrokerContentProvider.API api : BrokerContentProvider.API.values()) {
                final String version = api.getBrokerVersion();
                if (version != null) {
                    try {
                        final float candVersion = Float.parseFloat(version);
                        if (candVersion > protocolVersion) {
                            protocolVersion = candVersion;
                            stringVersion = version;
                        }
                    } catch (final NumberFormatException nfe) {
                        Logger.error(TAG + ":<static initialization>", "Invalid value for protocol version", nfe);
                    }
                }
            }
            return stringVersion;
        }

        /**
         * The key of maximum broker protocol version that client advertised.
         */
        public static final String CLIENT_ADVERTISED_MAXIMUM_BP_VERSION_KEY = "broker.protocol.version.name";

        /**
         * The key of minimum broker protocol version the client requires.
         * Broker will reject the request (in hello()) if its current version is older than its client's minimum version.
         */
        public static final String CLIENT_CONFIGURED_MINIMUM_BP_VERSION_KEY = "required.broker.protocol.version.name";

        /**
         * The key of negotiated broker protocol version between broker client and broker service.
         */
        public static final String NEGOTIATED_BP_VERSION_KEY = "common.broker.protocol.version.name";

        /**
         * String of broker protocol version with PRT support.
         */
        public static final String BROKER_PROTOCOL_VERSION = "v2";

        /**
         * String of broker skip cache.
         */
        public static final String BROKER_SKIP_CACHE = "skip.cache";

        /**
         * String of broker result returned.
         */
        public static final String BROKER_RESULT_RETURNED = "broker.result.returned";

        /**
         * String of broker redirect URI.
         */
        public static final String BROKER_REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

        /**
         * String of broker client ID.
         */
        public static final String BROKER_CLIENT_ID = "29d9ed98-a469-4536-ade2-f981bc1d605e";

        /**
         * Authtoken type string.
         */
        public static final String AUTHTOKEN_TYPE = "adal.authtoken.type";

        /**
         * String of broker final url.
         */
        public static final String BROKER_FINAL_URL = "adal.final.url";

        /**
         * String of the default browser package name.
         */
        public static final String DEFAULT_BROWSER_PACKAGE_NAME = "default.browser.package.name";

        /**
         * String of account initial request.
         */
        public static final String ACCOUNT_INITIAL_REQUEST = "account.initial.request";

        /**
         * String of account client id key.
         */
        public static final String ACCOUNT_CLIENTID_KEY = "account.clientid.key";

        /**
         * String of account client secret key.
         */
        public static final String ACCOUNT_CLIENT_SECRET_KEY = "account.client.secret.key";

        /**
         * String of account correlation id.
         */
        public static final String ACCOUNT_CORRELATIONID = "account.correlationid";

        /**
         * String of account prompt.
         */
        public static final String ACCOUNT_PROMPT = "account.prompt";

        /**
         * String of account extra query param.
         */
        public static final String ACCOUNT_EXTRA_QUERY_PARAM = "account.extra.query.param";

        /**
         * String of account claims.
         */
        public static final String ACCOUNT_CLAIMS = "account.claims";

        /**
         * Indicates whether the broker should bypass the accountmanager cache and use the refresh artiface (RT, FRT, PRT) to refresh access token.
         */
        public static final String BROKER_FORCE_REFRESH = "force.refresh";

        /**
         * String of account login hint.
         */
        public static final String ACCOUNT_LOGIN_HINT = "account.login.hint";

        /**
         * String of account resource.
         */
        public static final String ACCOUNT_RESOURCE = "account.resource";

        /**
         * String of account redirect.
         */
        public static final String ACCOUNT_REDIRECT = "account.redirect";

        /**
         * String of account authority.
         */
        public static final String ACCOUNT_AUTHORITY = "account.authority";

        /**
         * String of account refresh token.
         */
        public static final String ACCOUNT_REFRESH_TOKEN = "account.refresh.token";

        /**
         * String of account access token.
         */
        public static final String ACCOUNT_ACCESS_TOKEN = "account.access.token";

        /**
         * String of token expiration data for the broker account.
         */
        public static final String ACCOUNT_EXPIREDATE = "account.expiredate";

        /**
         * String of token result for the broker account.
         */
        public static final String ACCOUNT_RESULT = "account.result";

        /**
         * String of account remove tokens.
         */
        public static final String ACCOUNT_REMOVE_TOKENS = "account.remove.tokens";

        /**
         * String of account remove token value.
         */
        public static final String ACCOUNT_REMOVE_TOKENS_VALUE = "account.remove.tokens.value";

        /**
         * String of multi resource refresh token.
         */
        public static final String MULTI_RESOURCE_TOKEN = "account.multi.resource.token";

        /**
         * String of key for account name.
         */
        public static final String FLIGHT_INFO = "com.microsoft.identity.broker.flights";

        /**
         * String of key for account name.
         */
        public static final String ACCOUNT_NAME = "account.name";

        /**
         * String of key for account name.
         */
        public static final String ACCOUNT_HOME_ACCOUNT_ID = "account.home.account.id";

        /**
         * String of key for account id token.
         */
        public static final String ACCOUNT_IDTOKEN = "account.idtoken";

        /**
         * String of key for user id.
         */
        public static final String ACCOUNT_USERINFO_USERID = "account.userinfo.userid";

        /**
         * String of key for user id list.
         */
        public static final String ACCOUNT_USERINFO_USERID_LIST = "account.userinfo.userid.list";

        /**
         * String of key for given name.
         */
        public static final String ACCOUNT_USERINFO_GIVEN_NAME = "account.userinfo.given.name";

        /**
         * String of key for family name.
         */
        public static final String ACCOUNT_USERINFO_FAMILY_NAME = "account.userinfo.family.name";

        /**
         * String of key for identity provider.
         */
        public static final String ACCOUNT_USERINFO_IDENTITY_PROVIDER = "account.userinfo.identity.provider";

        /**
         * String of key for displayable id.
         */
        public static final String ACCOUNT_USERINFO_USERID_DISPLAYABLE = "account.userinfo.userid.displayable";

        /**
         * String of key for tenant id.
         */
        public static final String ACCOUNT_USERINFO_TENANTID = "account.userinfo.tenantid";

        /**
         * String of key for environment.
         */
        public static final String ACCOUNT_USERINFO_ENVIRONMENT = "account.userinfo.environment";

        /**
         * String of key for authority type.
         */
        public static final String ACCOUNT_USERINFO_AUTHORITY_TYPE = "account.userinfo.authority.type";

        /**
         * String of key for account id token record.
         */
        public static final String ACCOUNT_USERINFO_ID_TOKEN = "account.userinfo.id.token";

        /**
         * String of key for adal version.
         */
        public static final String ADAL_VERSION_KEY = "adal.version.key";

        /**
         * String of key for UIDs in the cache.
         */
        public static final String ACCOUNT_UID_CACHES = "account.uid.caches";

        /**
         * String of key for adding new account.
         */
        public static final String ACCOUNT_ADD_NEW = "account.add.new";

        /**
         * String of key for resolving account interruption.
         */
        public static final String ACCOUNT_RESOLVE_INTERRUPT = "account.resolve.interrupt";

        /**
         * String of key for user data prefix.
         */
        public static final String USERDATA_PREFIX = "userdata.prefix";

        /**
         * String of key for UID key.
         */
        public static final String USERDATA_UID_KEY = "calling.uid.key";

        /**
         * String of key for user data broker RT.
         */
        public static final String USERDATA_BROKER_RT = "userdata.broker.rt";

        /**
         * String of key for user data broker PRT, RT.
         */
        public static final String USERDATA_BROKER_PRT_RT = "userdata.broker.prt.rt";

        /**
         * String of key for user data broker PRT session key.
         */
        public static final String USERDATA_BROKER_PRT_SESSION_KEY = "userdata.broker.prt.session.key";

        /**
         * String of key for caller cache keys.
         */
        public static final String USERDATA_CALLER_CACHEKEYS = "userdata.caller.cachekeys";

        /**
         * String of caller cache key delimiter.
         */
        public static final String CALLER_CACHEKEY_PREFIX = "|";

        /**
         * String for pkeyauth sent in user agent string.
         */
        public static final String CLIENT_TLS_NOT_SUPPORTED = " PKeyAuth/1.0";

        /**
         * String of challenge request header.
         */
        public static final String CHALLENGE_REQUEST_HEADER = "WWW-Authenticate";

        /**
         * String of challenge response header.
         */
        public static final String CHALLENGE_RESPONSE_HEADER = "Authorization";

        /**
         * String of challenge response type.
         */
        public static final String CHALLENGE_RESPONSE_TYPE = "PKeyAuth";

        /**
         * String of challenge response token.
         */
        public static final String CHALLENGE_RESPONSE_TOKEN = "AuthToken";

        /**
         * String of challenge response context.
         */
        public static final String CHALLENGE_RESPONSE_CONTEXT = "Context";

        /**
         * String of authorization code grants via Proof Key for Code Exchange (PKCE).
         */
        public static final String PKCE_CHALLENGE = "PkceChallenge";

        /**
         * Certificate authorities are passed with delimiter.
         */
        public static final String CHALLENGE_REQUEST_CERT_AUTH_DELIMETER = ";";

        /**
         * Broker Host app package name.
         */
        public static final String BROKER_HOST_APP_PACKAGE_NAME = "com.microsoft.identity.testuserapp";

        /**
         * Azure Authenticator app package name.
         */
        public static final String AZURE_AUTHENTICATOR_APP_PACKAGE_NAME = "com.azure.authenticator";

        /**
         * Intune Company Portal app package name.
         */
        public static final String COMPANY_PORTAL_APP_PACKAGE_NAME = "com.microsoft.windowsintune.companyportal";

        /**
         * Signature info for Intune Company portal app that installs authenticator
         * component.
         */
        public static final String COMPANY_PORTAL_APP_RELEASE_SIGNATURE = "1L4Z9FJCgn5c0VLhyAxC5O9LdlE=";

        /**
         * Signature info for Azure authenticator app that installs authenticator
         * component.
         */
        public static final String AZURE_AUTHENTICATOR_APP_RELEASE_SIGNATURE = "ho040S3ffZkmxqtQrSwpTVOn9r0=";

        /**
         * Signature info for Azure authenticator app that installs authenticator
         * component.
         */
        public static final String AZURE_AUTHENTICATOR_APP_DEBUG_SIGNATURE = "N1jdcbbnKDr0LaFZlqdhXgm2luE=";

        /**
         * Signature info for Broker Host app that installs authenticator
         * component.
         */
        public static final String BROKER_HOST_APP_SIGNATURE = "1wIqXSqBj7w+h11ZifsnqwgyKrY=";


        /**
         * Teams IP Phones (Sakurai devices) is supported by Intune, but does not have a back button nor browser.
         * The only supported detection of this phone is the application install state.
         * The Microsoft Intune app depends on the browser opening the fwlink, and in the app manifest registers to handle the URL.
         * In the 1906 both apps will be installed on COBO devices, but the MDM CA link must open the browser to then open Microsoft Intune.
         * On IP Phones devices (without a browser) the Company Portal must be launched.
         * App name of Teams Phone app to detect it for the MDM Device CA redirect.
         */
        public static final String IPPHONE_APP_PACKAGE_NAME = "com.microsoft.skype.teams.ipphone";

        /**
         * Teams IP Phones (Sakurai devices) is supported by Intune, but does not have a back button nor browser.
         * The only supported detection of this phone is the application install state.
         * App signature of Teams Phone app to detect it for the MDM Device CA redirect.
         */
        public static final String IPPHONE_APP_SIGNATURE = "fcg80qvoM1YMKJZibjBwQcDfOno=";

        /**
         * The value for pkeyauth redirect.
         */
        public static final String PKEYAUTH_REDIRECT = "urn:http-auth:PKeyAuth";

        /**
         * Value of pkeyauth sent in the header.
         */
        public static final String CHALLENGE_TLS_INCAPABLE = "x-ms-PKeyAuth";

        /**
         * Value of supported pkeyauth version.
         */
        public static final String CHALLENGE_TLS_INCAPABLE_VERSION = ONE_POINT_ZERO;

        /**
         * Broker redirect prefix.
         */
        public static final String REDIRECT_PREFIX = "msauth";

        /**
         * Device Registration redirect url host name
         */
        public static final String DEVICE_REGISTRATION_REDIRECT_URI_HOSTNAME = "wpj";

        /**
         * Encoded delimiter for redirect.
         */
        public static final Object REDIRECT_DELIMETER_ENCODED = "%2C";

        /**
         * Prefix of redirect to open external browser.
         */
        public static final String BROWSER_EXT_PREFIX = "browser://";

        /**
         * Prefix in the redirect for installing broker apps.
         */
        public static final String BROWSER_EXT_INSTALL_PREFIX = "msauth://";

        /**
         * Prefix in the redirect from WebCP.
         */
        public static final String BROWSER_EXT_WEB_CP = "companyportal://";

        /**
         * Prefix for the Authenticator MFA linking.
         */
        public static final String AUTHENTICATOR_MFA_LINKING_PREFIX = "microsoft-authenticator://activatemfa";

        /**
         * Redirect URL from WebCP that should launch the Intune Company Portal app.
         */
        public static final String WEBCP_LAUNCH_COMPANY_PORTAL_URL = BROWSER_EXT_WEB_CP + "enrollment";

        /**
         * A query param indicating that this is an intune device CA link.
         */
        public static final String BROWSER_DEVICE_CA_URL_QUERY_STRING_PARAMETER = "&ismdmurl=1";

        /**
         * Activity name to launch company portal.
         */
        public static final String COMPANY_PORTAL_APP_LAUNCH_ACTIVITY_NAME = Broker.COMPANY_PORTAL_APP_PACKAGE_NAME + ".views.SplashActivity";

        /**
         * Redirect URI parameter key to get link to install broker
         */
        public static final String INSTALL_URL_KEY = "app_link";

        /**
         * Redirect URI parameter key to get the upn
         */
        public static final String INSTALL_UPN_KEY = "username";

        /**
         * PRT nonce.
         */
        public static final String PRT_NONCE = "nonce";

        /**
         * broker request type.
         */
        public static final String ACCOUNT_REQUEST_TYPE = "broker.request.type";

        /**
         * PRT response header.
         */
        public static final String PRT_RESPONSE_HEADER = "x-ms-RefreshTokenCredential";

        /**
         * caller information UID.
         */
        public static final String CALLER_INFO_UID = "caller.info.uid";

        /**
         * String for caller package.
         */
        public static final String CALLER_INFO_PACKAGE = "caller.info.package";

        /**
         * String to send Msal V2 Request params.
         */
        public static final String BROKER_REQUEST_V2 = "broker_request_v2";

        /**
         * String to send MSAL V2 Request params as gzip compressed byte array.
         */
        public static final String BROKER_REQUEST_V2_COMPRESSED = "broker_request_v2_compressed";

        /**
         * String to return Msal V2 response.
         */
        public static final String BROKER_RESULT_V2 = "broker_result_v2";

        /**
         * String to return MSA: V2 response as gzip compressed byte array.
         */
        public static final String BROKER_RESULT_V2_COMPRESSED = "broker_result_v2_compressed";

        /**
         * Represents the broker device mode boolean (true = shared device mode).
         * This is used to determine what PublicClientApplication MSAL will return to its caller.
         */
        public static final String BROKER_DEVICE_MODE = "broker_device_mode";

        /**
         * String for generate shr result.
         */
        public static final String BROKER_GENERATE_SHR_RESULT = "broker_generate_shr_result";

        /**
         * String to return a true if the request succeeded, false otherwise.
         */
        public static final String BROKER_REQUEST_V2_SUCCESS = "broker_request_v2_success";

        /**
         * String for ssl prefix.
         */
        public static final String REDIRECT_SSL_PREFIX = "https://";

        /**
         * Prefix in the redirect for PlayStore.
         */
        public static final String PLAY_STORE_INSTALL_PREFIX = "market://details?id=";

        /**
         * String for expiration buffer.
         * Integer for token expiration buffer. see {@link AuthenticationSettings#mExpirationBuffer}
         */
        public static final String EXPIRATION_BUFFER = "expiration.buffer";

        /**
         * String for authorization scope.
         */
        public static final String AUTH_SCOPE = "scope";

        /**
         * String for authorization state.
         */
        public static final String AUTH_STATE = "state";

        /**
         * String for authorization response type.
         */
        public static final String AUTH_RESPONSE_TYPE = "response_type";

        /**
         * String for library name.
         */
        public static final String LIB_NAME = "library_name";

        /**
         * String for library version.
         */
        public static final String LIB_VERSION = "library_version";

        /**
         * String for the package name of the client app.
         */
        public static final String CLIENT_APP_PACKAGE_NAME = "client_app_package_name";

        /**
         * String of account environment key.
         */
        public static final String ENVIRONMENT = "environment";

        /**
         * String to return account list from broker.
         */
        public static final String BROKER_ACCOUNTS = "broker_accounts";

        /**
         * String to return account list as compressed json.
         */
        public static final String BROKER_ACCOUNTS_COMPRESSED = "broker_accounts_compressed";

        public static final String BROKER_KEYSTORE_SYMMETRIC_KEY = "broker_keystore_symmetric_key";

        /**
         * String indicating a broker flow that Authenticator should route to.
         * See BrokerAccountManagerOperation for more info.
         */
        public static final String BROKER_ACCOUNT_MANAGER_OPERATION_KEY = "com.microsoft.broker_accountmanager_operation_key";

        /**
         * Boolean to return when a broker account is successfully removed.
         */
        public static final String REMOVE_BROKER_ACCOUNT_SUCCEEDED = "remove_broker_account_succeeded";

        /**
         * Boolean to return when a Broker RT is successfully updated.
         */
        public static final String UPDATE_BROKER_RT_SUCCEEDED = "update_broker_rt_succeeded";

        /**
         * Boolean to return when a Broker RT is successfully updated.
         */
        public static final String SET_FLIGHTS_SUCCEEDED = "set_flights_succeeded";

        /**
         * All of the active flights.
         */
        public static final String GET_FLIGHTS_RESULT = "active_flights";

        /**
         * Time out for the AccountManager's remove account operation in broker.
         */
        public static final int ACCOUNT_MANAGER_REMOVE_ACCOUNT_TIMEOUT_IN_MILLISECONDS = 5000;

        /**
         * The Bundle key name of serialized parameters for the PoP auth scheme.
         */
        public static final String AUTH_SCHEME_PARAMS_POP = "pop_parameters";

        /**
         * Bundle identifiers for x-ms-clitelem info.
         */
        public static final class CliTelemInfo {

            private static final String PREFIX = "cliteleminfo.";

            /**
             * Bundle id for server errors.
             */
            public static final String SERVER_ERROR = PREFIX + "server_error";

            /**
             * Bundle id for server suberrors.
             */
            public static final String SERVER_SUBERROR = PREFIX + "server_suberror";

            /**
             * Bundle id for refresh token age.
             */
            public static final String RT_AGE = PREFIX + "rt_age";

            /**
             * Bundle id for spe_ring info.
             */
            public static final String SPE_RING = PREFIX + "spe_ring";
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class OAuth2Scopes {

        /**
         * Scope to get get open id connect ID token
         */
        public static final String OPEN_ID_SCOPE = "openid";

        /**
         * Scope to give the app access to get resources on behalf of user for an extended time.
         * App can receive refresh tokens using this scope.
         */
        public static final String OFFLINE_ACCESS_SCOPE = "offline_access";

        /**
         * Scope to get user profile information as a part Id token
         */
        public static final String PROFILE_SCOPE = "profile";

        /**
         * Custom scope used to get PRT
         */
        public static final String AZA_SCOPE = "aza";

        /**
         * Scope to get email claim as part of the ID Token
         */
        public static final String EMAIL = "email";

        /**
         * Used in the interrupt flow. See BrokerJoinedAccountController for more info.
         * The BRT request made with this resourceID will contain the updated claim acquired in the interrupt flow performed before it.
         */
        public static final String CLAIMS_UPDATE_RESOURCE = "urn:aad:tb:update:prt/.default";
    }

    /**
     * Represents Broker operations that should be invoked by Authenticator.java (MSAL-Broker AccountManager flow).
     * See MicrosoftAuthServiceOperation for more info.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class BrokerAccountManagerOperation {

        public static final String HELLO = "HELLO";

        public static final String GET_ACCOUNTS = "GET_ACCOUNTS";

        public static final String ACQUIRE_TOKEN_SILENT = "ACQUIRE_TOKEN_SILENT";

        public static final String GET_INTENT_FOR_INTERACTIVE_REQUEST = "GET_INTENT_FOR_INTERACTIVE_REQUEST";

        public static final String REMOVE_ACCOUNT = "REMOVE_ACCOUNT";

        public static final String GET_DEVICE_MODE = "GET_DEVICE_MODE";

        public static final String GET_CURRENT_ACCOUNT = "GET_CURRENT_ACCOUNT";

        public static final String REMOVE_ACCOUNT_FROM_SHARED_DEVICE = "REMOVE_ACCOUNT_FROM_SHARED_DEVICE";

        public static final String GENERATE_SHR = "GENERATE_SHR";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class BrokerContentProvider {
        /**
         * URI Scheme constant to invoke content provider.
         */
        public static final String CONTENT_SCHEME = "content://";

        /**
         * URI Authority constant for content provider.
         * <p>
         * This is will be pre-fixed by com.azure.authenticator or com.microsoft.windowsintune.companyportal
         * depending on which ever is the active broker.
         */
        public static final String AUTHORITY = "microsoft.identity.broker";

        private static final String VERSION_1 = ONE_POINT_ZERO;
        private static final String VERSION_3 = "3.0";
        private static final String VERSION_6 = "6.0";
        private static final String VERSION_7 = "7.0";
        private static final String BROKER_VERSION_1 = ONE_POINT_ZERO;
        private static final String BROKER_VERSION_2 = TWO_POINT_ZERO;

        /**
         * Tie the API paths and codes into a single object structure to stop us from having to keep
         * them in sync.  This is designed to pull all the parts of the API definition into a single
         * place, so that we only need to make updates in one location, and it's clearer what we need
         * to do when adding new APIs.
         *
         *
         */
        @Getter
        @Accessors(prefix = "m")
        @AllArgsConstructor
        public enum API {
            MSAL_HELLO(MSAL_HELLO_PATH, null, VERSION_3),
            ACQUIRE_TOKEN_INTERACTIVE(MSAL_ACQUIRE_TOKEN_INTERACTIVE_PATH, null, VERSION_3),
            ACQUIRE_TOKEN_SILENT(MSAL_ACQUIRE_TOKEN_SILENT_PATH, null, VERSION_3),
            GET_ACCOUNTS(MSAL_GET_ACCOUNTS_PATH, null, VERSION_3),
            REMOVE_ACCOUNT(MSAL_REMOVE_ACCOUNT_PATH, null, VERSION_3),
            GET_CURRENT_ACCOUNT_SHARED_DEVICE(MSAL_GET_CURRENT_ACCOUNT_SHARED_DEVICE_PATH, null, VERSION_3),
            GET_DEVICE_MODE(MSAL_GET_DEVICE_MODE_PATH, null, VERSION_3),
            SIGN_OUT_FROM_SHARED_DEVICE(MSAL_SIGN_OUT_FROM_SHARED_DEVICE_PATH, null, VERSION_3),
            GENERATE_SHR(GENERATE_SHR_PATH, null, VERSION_6),
            BROKER_HELLO(BROKER_API_HELLO_PATH, BROKER_VERSION_1, null),
            BROKER_GET_ACCOUNTS(BROKER_API_GET_BROKER_ACCOUNTS_PATH, BROKER_VERSION_1, null),
            BROKER_REMOVE_ACCOUNT(BROKER_API_REMOVE_BROKER_ACCOUNT_PATH, BROKER_VERSION_1, null),
            BROKER_UPDATE_BRT(BROKER_API_UPDATE_BRT_PATH, BROKER_VERSION_1, null),
            BROKER_ADD_FLIGHTS(BROKER_API_ADD_FLIGHTS_PATH, BROKER_VERSION_2, null),
            BROKER_SET_FLIGHTS(BROKER_API_SET_FLIGHTS_PATH, BROKER_VERSION_2, null),
            BROKER_GET_FLIGHTS(BROKER_API_GET_FLIGHTS_PATH, BROKER_VERSION_2, null),
            GET_SSO_TOKEN(GET_SSO_TOKEN_PATH, null, VERSION_7),
            UNKNOWN(null, null, null)
                ;
            /**
             * The content provider path that the API exists behind.
             */
            private String mPath;
            /**
             * The broker-host-to-broker protocol version that the API requires.
             */
            private String mBrokerVersion;
            /**
             * The msal-to-broker version that the API requires.
             */
            private String mMsalVersion;
        }
        /**
         * URI Path constant for MSAL-to-Broker hello request using ContentProvider.
         */
        public static final String MSAL_HELLO_PATH = "/hello";

        /**
         * URI Path constant for MSAL-to-Broker acquireTokenInteractive request using ContentProvider.
         */
        public static final String MSAL_ACQUIRE_TOKEN_INTERACTIVE_PATH = "/acquireTokenInteractive";

        /**
         * URI Path constant for MSAL-to-Broker acquireTokenSilent request using ContentProvider.
         */
        public static final String MSAL_ACQUIRE_TOKEN_SILENT_PATH = "/acquireTokenSilent";

        /**
         * URI Path constant for MSAL-to-Broker getAccounts request using ContentProvider.
         */
        public static final String MSAL_GET_ACCOUNTS_PATH = "/getAccounts";

        /**
         * URI Path constant for MSAL-to-Broker removeAccounts request using ContentProvider.
         */
        public static final String MSAL_REMOVE_ACCOUNT_PATH = "/removeAccounts";

        /**
         * URI Path constant for MSAL-to-Broker getCurrentAccountSharedDevice request using ContentProvider.
         */
        public static final String MSAL_GET_CURRENT_ACCOUNT_SHARED_DEVICE_PATH = "/getCurrentAccountSharedDevice";

        /**
         * URI Path constant for MSAL-to-Broker getDeviceMode request using ContentProvider.
         */
        public static final String MSAL_GET_DEVICE_MODE_PATH = "/getDeviceMode";

        /**
         * URI Path constant for MSAL-to-Broker signOutFromSharedDevice request using ContentProvider.
         */
        public static final String MSAL_SIGN_OUT_FROM_SHARED_DEVICE_PATH = "/signOutFromSharedDevice";

        /**
         * URI Path constant for MSAL-to-Broker generateShr request using ContentProvider.
         */
        public static final String GENERATE_SHR_PATH = "/generateShr";

        /**
         * URI Path constant for BrokerApi-to-Broker hello request using ContentProvider.
         */
        public static final String BROKER_API_HELLO_PATH = "/brokerApi/hello";

        /**
         * URI Path constant for BrokerApi-to-Broker getBrokerAccounts request using ContentProvider.
         */
        public static final String BROKER_API_GET_BROKER_ACCOUNTS_PATH = "/brokerApi/getBrokerAccounts";

        /**
         * URI Path constant for BrokerApi-to-Broker removeBrokerAccount request using ContentProvider.
         */
        public static final String BROKER_API_REMOVE_BROKER_ACCOUNT_PATH = "/brokerApi/removeBrokerAccount";

        /**
         * URI Path constant for BrokerApi-to-Broker updateBrt request using ContentProvider.
         */
        public static final String BROKER_API_UPDATE_BRT_PATH = "/brokerApi/updateBrt";

        /**
         * Broker api path constant for adding flight information.
         */
        public static final String BROKER_API_ADD_FLIGHTS_PATH = "/brokerApi/addFlights";

        /**
         * Broker api path constant for adding flight information.
         */
        public static final String BROKER_API_GET_FLIGHTS_PATH = "/brokerApi/getFlights";

        /**
         * Broker api path constant for adding flight information.
         */
        public static final String BROKER_API_SET_FLIGHTS_PATH = "/brokerApi/setFlights";

        /**
         * Broker api path constant for adding flight information.
         */
        public static final String GET_SSO_TOKEN_PATH = "/ssoToken";

        /**
         * BrokerContentProvider URI code constant for MSAL-to-Broker hello request.
         */
        public static final int MSAL_HELLO_URI_CODE = 1;

        /**
         * BrokerContentProvider URI code constant for MSAL-to-Broker acquireTokenInteractive request.
         */
        public static final int MSAL_ACQUIRE_TOKEN_INTERACTIVE_CODE = 2;

        /**
         * BrokerContentProvider URI code constant for MSAL-to-Broker acquireTokenSilent request.
         */
        public static final int MSAL_ACQUIRE_TOKEN_SILENT_CODE = 3;

        /**
         * BrokerContentProvider URI code constant for MSAL-to-Broker getAccounts request.
         */
        public static final int MSAL_GET_ACCOUNTS_CODE = 4;

        /**
         * BrokerContentProvider URI code constant for MSAL-to-Broker removeAccounts request.
         */
        public static final int MSAL_REMOVE_ACCOUNTS_CODE = 5;

        /**
         * BrokerContentProvider URI code constant for MSAL-to-Broker getCurrentAccountSharedDevice request.
         */
        public static final int MSAL_GET_CURRENT_ACCOUNT_SHARED_DEVICE_CODE = 6;

        /**
         * BrokerContentProvider URI code constant for MSAL-to-Broker getDeviceMode request.
         */
        public static final int MSAL_GET_DEVICE_MODE_CODE = 7;

        /**
         * BrokerContentProvider URI code constant for MSAL-to-Broker signOutFromSharedDevice request.
         */
        public static final int MSAL_SIGN_OUT_FROM_SHARED_DEVICE_CODE = 8;

        /**
         * BrokerContentProvider URI code constant for BrokerApi-to-Broker signOutFromSharedDevice request.
         */
        public static final int BROKER_API_HELLO_URI_CODE = 9;

        /**
         * BrokerContentProvider URI code constant for BrokerApi-to-Broker getBrokerAccounts request.
         */
        public static final int BROKER_API_GET_BROKER_ACCOUNTS_CODE = 10;

        /**
         * BrokerContentProvider URI code constant for BrokerApi-to-Broker removeBrokerAccount request.
         */
        public static final int BROKER_API_REMOVE_BROKER_ACCOUNT_CODE = 11;

        /**
         * BrokerContentProvider URI code constant for BrokerApi-to-Broker updateBrt request.
         */
        public static final int BROKER_API_UPDATE_BRT_CODE = 12;

        /**
         * BrokerContentProvider URI code constant for MSAL-to-Broker generateSHR request.
         */
        public static final int MSAL_GENERATE_SHR_CODE = 13;

    }

    public static final class AuthorizationIntentKey {

        public static final String AUTH_INTENT = "com.microsoft.identity.auth.intent";

        public static final String REQUEST_URL = "com.microsoft.identity.request.url";

        public static final String REDIRECT_URI = "com.microsoft.identity.request.redirect.uri";

        public static final String REQUEST_HEADERS = "com.microsoft.identity.request.headers";

        public static final String POST_PAGE_LOADED_URL = "com.microsoft.identity.post.page.loaded.url";

        public static final String AUTHORIZATION_AGENT = "com.microsoft.identity.client.authorization.agent";

        public static final String REQUEST_ID = "com.microsoft.identity.request.id";

        public static final String AUTHORIZATION_FINAL_URL = "com.microsoft.identity.client.final.url";

        public static final String RESULT_CODE = "com.microsoft.identity.client.result.code";

        public static final String REQUEST_CODE = "com.microsoft.identity.client.request.code";

        public static final String REQUEST_CANCELLED_BY_USER = "com.microsoft.identity.client.request.cancelled.by.user";

        public static final String WEB_VIEW_ZOOM_CONTROLS_ENABLED = "com.microsoft.identity.web.view.zoom.controls.enabled";

        public static final String WEB_VIEW_ZOOM_ENABLED = "com.microsoft.identity.web.view.zoom.enabled";
    }

    public static final class AuthorizationIntentAction {

        /**
         * an intent action specifying that the current interactive action should be cancelled.
         */
        public static final String CANCEL_INTERACTIVE_REQUEST = "cancel_interactive_request";

        /**
         * an intent action specifying that the intent contains authorization results.
         */
        public static final String RETURN_INTERACTIVE_REQUEST_RESULT = "return_interactive_request_result";
    }

    /**
     * Represents the oauth2 error code.
     */
    public static final class OAuth2ErrorCode {
        /**
         * Oauth2 error code invalid_grant.
         */
        public static final String INVALID_GRANT = "invalid_grant";

        /**
         * Oauth2 error code unauthorized_client.
         */
        public static final String UNAUTHORIZED_CLIENT = "unauthorized_client";

        /**
         * The refresh token used to redeem access token is invalid and auth code request is needed.
         * This is deprecated in V2, but is kept here due to this bug https://identitydivision.visualstudio.com/Engineering/_workitems/edit/597793.
         */
        public static final String INTERACTION_REQUIRED = "interaction_required";
    }

    /**
     * Represents the oauth2 sub error code.
     */
    public static final class OAuth2SubErrorCode {

        /**
         * Oauth2 suberror code for unauthorized_client.
         * <p>
         * Suberror code when Intune App Protection Policy is required.
         */
        public static final String PROTECTION_POLICY_REQUIRED = "protection_policy_required";

        /**
         * Oauth2 suberror code for invalid_grant.
         * <p>
         * Suberror code when token is expired or invalid for all resources
         * and scopes and shouldn't be retried again as-is.
         */
        public static final String BAD_TOKEN = "bad_token";

        /**
         * Oauth2 suberror code for invalid_grant.
         * <p>
         * Suberror code when failed to do device authentication during a token request.
         * Broker should make a request to DRS to get the current device status and act accordingly.
         */
        public static final String DEVICE_AUTHENTICATION_FAILED = "device_authentication_failed";

        /**
         * Oauth2 suberror code for invalid_grant.
         * <p>
         * SubError code for cases when client not in the Microsoft first party family group
         * redeems auth code or refresh token given to a client in the family.
         */
        public static final String CLIENT_MISMATCH = "client_mismatch";

        /**
         * Oauth2 suberror code for invalid_grant.
         * <p>
         * Conditional access suberror code when a policy enforces token lifetime.
         */
        public static final String TOKEN_EXPIRED = "token_expired";

        /**
         * Oauth2 suberror code for invalid_grant.
         * <p>
         * Conditional access suberror code which indicates a simple action is required by the end user, like MFA.
         */
        public static final String BASIC_ACTION = "basic_action";

        /**
         * Oauth2 suberror code for invalid_grant.
         * <p>
         * Conditional access suberror code which indicates additional action is
         * required that is in the user control, but is outside of the sign in session.
         * For example, enroll in MDM or register install an app that uses Intune app protection.
         */
        public static final String ADDITIONAL_ACTION = "additional_action";

        /**
         * Oauth2 suberror code for invalid_grant.
         * <p>
         * Conditional access suberror code where user will be shown an informational
         * message with no immediate remediation steps.
         * For example access was blocked due to location or the device is not domain joined.
         */
        public static final String MESSAGE_ONLY = "message_only";

        /**
         * Oauth2 suberror code for invalid_grant.
         * <p>
         * OpenId connect suberror code, where user consent is required.
         */
        public static final String CONSENT_REQUIRED = "consent_required";

        /**
         * Oauth2 suberror code for invalid_grant.
         * <p>
         * Custom sub error that notifies the user that their password has expired.
         */
        public static final String USER_PASSWORD_EXPIRED = "user_password_expired";
    }

    /**
     * HTTP header fields.
     */
    public static final class HeaderField {

        /**
         * @see <a href="https://tools.ietf.org/html/rfc1945#appendix-D.2.1">RFC-1945</a>
         */
        public static final String ACCEPT = "Accept";

        /**
         * Header used to track SPE Ring for telemetry.
         */
        public static final String X_MS_CLITELEM = "x-ms-clitelem";
    }

    /**
     * Identifiers for file formats and format contents.
     */
    public static final class MediaType {

        /**
         * @see <a href="https://tools.ietf.org/html/rfc7159">RFC-7159</a>
         */
        public static final String APPLICATION_JSON = "application/json";
    }

    public static final class TelemetryEvents {
        public static final String DECRYPTION_ERROR = "decryption_error_v2";

        public static final String KEYCHAIN_WRITE_START = "keychain_write_v2_start";

        public static final String KEYCHAIN_WRITE_END = "keychain_write_v2_end";

        public static final String KEYCHAIN_READ_START = "keychain_read_v2_start";

        public static final String KEYCHAIN_READ_END = "keychain_read_v2_end";

        public static final String KEY_RETRIEVAL_START = "key_retrieval_v2_start";

        public static final String KEY_RETRIEVAL_END = "key_retrieval_v2_end";

        public static final String KEY_DISTRIBUTION_START = "key_distribution_v2_start";

        public static final String KEY_DISTRIBUTION_END = "key_distribution_v2_end";

        public static final String KEY_CREATED = "key_created_v2";

        public static final String SHARED_DEVICE_REGISTERED = "shared_device_registered";

        public static final String USER_SIGNED_INTO_SHARED_DEVICE = "user_signed_into_shared_device";

        public static final String USER_SIGNED_OUT_FROM_SHARED_DEVICE = "user_signed_out_from_shared_device";
    }

    /**
     * Sdk platform and Sdk version fields.
     */
    public static final class SdkPlatformFields {
        /**
         * The String representing the sdk platform.
         */
        public static final String PRODUCT = "x-client-SKU";

        /**
         * The String representing the sdk version.
         */
        public static final String VERSION = "x-client-Ver";

        /**
         * The String representing the MSAL SdkType.
         */
        public static final String PRODUCT_NAME_MSAL = "MSAL.Android";

        /**
         * The String representing the MSAL.CPP SdkType.
         */
        public static final String PRODUCT_NAME_MSAL_CPP = "MSAL.xplat.Android";
    }

}
