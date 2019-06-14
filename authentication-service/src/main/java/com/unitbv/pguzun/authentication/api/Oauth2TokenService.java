package com.unitbv.pguzun.authentication.api;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * This is {@link OAuth2AccessToken AccessToken} supply service.
 */
public interface Oauth2TokenService {
    /**
     * Gets an {@link OAuth2AccessToken accessToken}} for the given
     * {@link OAuth2Authentication authentication}}.
     * 
     * @param authentication of oauth2 nature.
     * @return the obtained OAuth2AccessToken. The serialized access token is in
     *         {@link OAuth2AccessToken#getValue()}}.
     * @throws AuthenticationException if the the given authentication is
     *                                 {@code null} or not
     *                                 {@link OAuth2Authentication#isAuthenticated()
     *                                 authenticated}
     */
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) throws AuthenticationException;

}
