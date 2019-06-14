package com.unitbv.pguzun.authentication.service;

import java.util.Optional;

import com.unitbv.pguzun.authentication.api.OauthUser;

public interface OauthUserRepository{
    
    Optional<OauthUser> findOne(String identifier);
    
    void save(String identifier, OauthUser user);
}
