package com.unitbv.pguzun.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.provisioning.UserDetailsManager;

import com.unitbv.pguzun.authentication.service.FederatedOauthUserService;
import com.unitbv.pguzun.authentication.service.InMemoryUserRepository;
import com.unitbv.pguzun.authentication.service.LocalUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.antMatcher("/**")
            .authorizeRequests().anyRequest().authenticated()
        .and().oauth2Login()
        .userInfoEndpoint().userService(oAuth2UserService()).and()
        .and().httpBasic().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
        .and().csrf().disable();
        // @formatter:on
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new FederatedOauthUserService(new DefaultOAuth2UserService(), new InMemoryUserRepository());
    }

    @Bean
    public UserDetailsManager userDetailService() {
        return new LocalUserService(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Value("${signedjwt.public.key}")
    private String publicKey;

    @Bean
    public TokenStore tokenStore() throws Exception {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() throws Exception {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(publicKey);
        return converter;
    }

    @Autowired
    public void configureSigner(JwtAccessTokenConverter converter, @Value("${signedjwt.private.key}") String privateKey)
            throws Exception {
        converter.setSigningKey(privateKey);
    }
}
