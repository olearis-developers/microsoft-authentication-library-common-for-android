package com.microsoft.identity.common.internal.providers.oauth2;

import android.content.Context;

/**
 * Class for managing the tokens saved locally on a device
 */
public abstract class OAuth2TokenCache {

    protected Context mContext;

    /**
     * Constructs a new OAuth2TokenCache.
     *
     * @param context The Application Context of the consuming app.
     */
    public OAuth2TokenCache(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * Saves the credentials and tokens returned by the service to the cache.
     *
     * @param oAuth2Strategy The strategy used to create the token request.
     * @param request        The request used to acquire tokens and credentials.
     * @param response       The response received from the IdP/STS.
     */
    public abstract void saveTokens(final OAuth2Strategy oAuth2Strategy,
                                    final AuthorizationRequest request,
                                    final TokenResponse response
    );
}
