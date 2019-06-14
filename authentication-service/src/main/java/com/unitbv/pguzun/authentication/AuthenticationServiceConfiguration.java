package com.unitbv.pguzun.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.unitbv.pguzun.authentication.api.Oauth2TokenService;
import com.unitbv.pguzun.authentication.service.OAuth2AccessTokenTokenService;
import com.unitbv.pguzun.authentication.web.AuthenticationController;

@Configuration
@Import( SecurityConfiguration.class)
@EnableOAuth2Client
public class AuthenticationServiceConfiguration {

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;
    @Value("${access-token.validity-seconds:3600}") 
    private int accessTokenValiditySeconds;

    @Bean
    public AuthenticationController authenticationController() throws Exception {
        return new AuthenticationController(oauth2TokenService());
    }

    @Bean
    public Oauth2TokenService oauth2TokenService() throws Exception {
        return new OAuth2AccessTokenTokenService(accessTokenValiditySeconds, accessTokenConverter);
    }
}
