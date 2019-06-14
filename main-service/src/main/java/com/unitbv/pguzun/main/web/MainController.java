package com.unitbv.pguzun.main.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @RequestMapping("/")
    public String home(@AuthenticationPrincipal Authentication auth) {
        return "<h1>Main-service is up. Hello " + auth.getName() + " user. You have the following claims "
                + auth.getAuthorities() + "</h1>";
    }

}
