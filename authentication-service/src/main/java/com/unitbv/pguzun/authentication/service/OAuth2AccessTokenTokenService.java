package com.unitbv.pguzun.authentication.service;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.unitbv.pguzun.authentication.api.Oauth2TokenService;

public class OAuth2AccessTokenTokenService implements Oauth2TokenService {

    protected final static int DEFAULT_ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60;
    private final int accessTokenValiditySeconds;
    private final TokenEnhancer accessTokenEnhancer;

    public OAuth2AccessTokenTokenService(Integer accessTokenValiditySeconds, TokenEnhancer accessTokenEnhancer) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds == null ? DEFAULT_ACCESS_TOKEN_VALIDITY_SECONDS
                : accessTokenValiditySeconds;
        this.accessTokenEnhancer = Objects.requireNonNull(accessTokenEnhancer, "TokenEnhancer can not be null.");
    }

    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InsufficientAuthenticationException("You are not authenticated.");
        }

        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
        token.setExpiration(getAccessTokenValidity());

        token.setAdditionalInformation(getAdditionalInformation(authentication));

        return accessTokenEnhancer.enhance(token, authentication);
    }

    private Map<String, Object> getAdditionalInformation(OAuth2Authentication authentication) {
        if (authentication.getUserAuthentication() instanceof OAuth2AuthenticationToken) {
            OAuth2User principal = ((OAuth2AuthenticationToken) authentication.getUserAuthentication()).getPrincipal();
            return principal == null ? Collections.emptyMap() : principal.getAttributes();
        }

        return Collections.emptyMap();
    }

    private Date getAccessTokenValidity() {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.add(Calendar.SECOND, accessTokenValiditySeconds);

        return calendar.getTime();
    }
}
