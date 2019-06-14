package com.unitbv.pguzun.authentication.api;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * An OauthUser represents a saved copy of the user logged in through OAuth
 * protocol. Once user is saved you can see him in the system and add additional
 * authorities or claims.
 */
public final class OauthUser {

    /**
     * These are the claims from the OpenID connect ID Token. You can read more
     * https://openid.net/specs/openid-connect-core-1_0.html#Claims
     */
    private final Map<String, Object> claims;
    /**
     * The authorities is a set of roles and permissions for an OAuth user. Initially authorities have a singleton
     * "ROLE_USER".
     */
    private final Collection<String> authorities;

    public OauthUser(Collection<String> authorities, Map<String, Object> claims) {
        this.authorities = authorities == null ? ImmutableSet.of() : ImmutableSet.copyOf(authorities);
        this.claims = claims == null ? ImmutableMap.of() : ImmutableMap.copyOf(claims);
    }

    public OauthUser(OAuth2User user) {
        this(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toSet()), user.getAttributes());
    }

    public Map<String, Object> getAttributes() {
        return claims;
    }

    public Collection<String> getAuthorities() {
        return authorities;
    }

    public OAuth2User asOAuth2User() {
        Set<GrantedAuthority> authorities = this.authorities.stream().map(SimpleGrantedAuthority::new).collect(toSet());
        return new DefaultOAuth2User(authorities, claims, "name");
    }
}
