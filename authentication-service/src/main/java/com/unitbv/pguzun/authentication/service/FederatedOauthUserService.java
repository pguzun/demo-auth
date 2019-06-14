package com.unitbv.pguzun.authentication.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.unitbv.pguzun.authentication.api.OauthUser;

public class FederatedOauthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    final String SUBJECT_KEY = "sub";
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegateService;
    private final OauthUserRepository oauthUserRepository;

    public FederatedOauthUserService(OAuth2UserService<OAuth2UserRequest, OAuth2User> delegateService,
            OauthUserRepository oauthUserRepository) {
        this.delegateService = Objects.requireNonNull(delegateService, "The delegate user service can not be null");
        this.oauthUserRepository = Objects.requireNonNull(oauthUserRepository, "The user repository can not be null");
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User remotelyLoadedUser = delegateService.loadUser(userRequest);

        String username = String.valueOf(remotelyLoadedUser.getAttributes().get(SUBJECT_KEY));
        Optional<OAuth2User> localOauth2User = oauthUserRepository.findOne(username).map(OauthUser::asOAuth2User);

        if (!localOauth2User.isPresent()) {

            oauthUserRepository.save(username, new OauthUser(remotelyLoadedUser));
            return remotelyLoadedUser;
        }

        Set<GrantedAuthority> authorities = new HashSet<>(); 
        authorities.addAll(remotelyLoadedUser.getAuthorities());
        authorities.addAll(localOauth2User.get().getAuthorities());
        
        // on attributes first local values then remote
        Map<String, Object> attributes = new HashMap<>();
        attributes.putAll(localOauth2User.get().getAttributes());
        attributes.putAll(remotelyLoadedUser.getAttributes());

        return new DefaultOAuth2User(authorities, attributes, "name");
    }
}
