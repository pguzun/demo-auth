package com.unitbv.pguzun.authentication.service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.unitbv.pguzun.authentication.api.OauthUser;

//TODO Remove this as soon users can be stored
public class InMemoryUserRepository implements OauthUserRepository {

    private final Map<String, OauthUser> map = new ConcurrentHashMap<>();

    public InMemoryUserRepository() {
        OauthUser specialUser = new OauthUser(ImmutableSet.of("main-service.access"),
                ImmutableMap.of("name", "auth0|5bfd4177deb7365f7c2e3ff2"));
        map.put("auth0|5bfd4177deb7365f7c2e3ff2", specialUser);
    }

    @Override
    public Optional<OauthUser> findOne(String identifier) {
        return Optional.ofNullable(map.get(identifier));
    }

    @Override
    public void save(String identifier, OauthUser user) {
        map.put(identifier, user);
    }
}
