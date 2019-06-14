package com.unitbv.pguzun.authentication.api;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

/**
 * This is an an User identity. It has authentication credentials and a list of
 * authorities as roles and permissions.
 */
public final class LocalUser {
    /**
     * The username of the user.
     */
    private final String username;
    /**
     * The password hash for user's password.
     */
    private final String password;
    /**
     * The authorities is set of roles and permissions of the user.
     */
    private final Collection<String> authorities;

    public LocalUser(String username, String password, Collection<String> authorities) {
        Preconditions.checkArgument(!StringUtils.isEmpty(username), "The username cant be null or empty");
        Preconditions.checkArgument(password != null, "The password cant be null");

        this.authorities = authorities == null ? ImmutableSet.of() : ImmutableSet.copyOf(authorities);
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Collection<String> getAuthorities() {
        return authorities;
    }

    public UserDetails asUserDetails() {
        Set<GrantedAuthority> authorities = this.authorities.stream().map(SimpleGrantedAuthority::new).collect(toSet());
        return new User(username, password, authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, authorities);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof LocalUser)) {
            return false;
        }
        LocalUser other = (LocalUser) obj;

        return Objects.equals(username, other.username) && Objects.equals(password, other.password)
                && Objects.equals(authorities, other.authorities);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("username", username).add("password", "****")
                .add("authorities", authorities).toString();
    }

}
