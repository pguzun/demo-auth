package com.unitbv.pguzun.authentication.web;

import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.unitbv.pguzun.authentication.api.Oauth2TokenService;

@Controller
public class AuthenticationController {

    private Oauth2TokenService oauth2TokenService;

    public AuthenticationController(Oauth2TokenService oauth2TokenService ) {
        this.oauth2TokenService = Objects.requireNonNull(oauth2TokenService, "Oauth2TokenService cannot be null.");
    }

    @RequestMapping("/oauth/token")
    public ModelAndView authenticate(Model model, HttpServletResponse response) throws Exception {

        response.addHeader(HttpHeaders.AUTHORIZATION, getAuthorizationHeader());
        return new ModelAndView("/wellcome.html");
    }

    @RequestMapping(method = RequestMethod.POST, path = "/oauth/token")
    public ResponseEntity<OAuth2AccessToken> authenticate() throws Exception {
        return ResponseEntity.noContent().header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader()).build();
    }

    private String getAuthorizationHeader() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final AuthorizationRequest authorizationRequest = new AuthorizationRequest();
        authorizationRequest.setApproved(true);
        OAuth2AccessToken accessToken = oauth2TokenService
                .getAccessToken(new OAuth2Authentication(authorizationRequest.createOAuth2Request(), authentication));

        return accessToken.getTokenType() + " " + accessToken.getValue();
    }

    protected Oauth2TokenService getOauth2TokenService() {
        return oauth2TokenService;
    }
}