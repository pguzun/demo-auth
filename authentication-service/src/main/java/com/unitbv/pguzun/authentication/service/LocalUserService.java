package com.unitbv.pguzun.authentication.service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import com.google.common.collect.ImmutableList;
import com.unitbv.pguzun.authentication.api.LocalUser;

//TODO Delete/refactor this class once persistence is in place.
public class LocalUserService implements UserDetailsManager {

    private final Map<String, LocalUser> map = new ConcurrentHashMap<>();

    public LocalUserService(PasswordEncoder passwordEncoder) {
        LocalUser testUser = new LocalUser("test_user", passwordEncoder.encode("7e48272b-e48d-442a-8f0f-dc6526f62202"),
                ImmutableList.of("ROLE_ADMIN"));
        map.put(testUser.getUsername(), testUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(map.get(username)).map(LocalUser::asUserDetails).orElse(null);
    }

    @Override
    public void createUser(UserDetails user) {
        throw new UnsupportedOperationException("Creating clients is not supported yet.");
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Updating clients is not supported yet.");
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Deleting clients is not supported yet.");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // no op
    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}
