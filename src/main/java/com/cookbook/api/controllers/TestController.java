package com.cookbook.api.controllers;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class TestController {
    @GetMapping("/test")
    public Map<String, Object> currentUser(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return oAuth2AuthenticationToken.getPrincipal().getAttributes();
    }

    @GetMapping("/login/oauth2/code/google")
    public String handleGoogleCallback(OAuth2AuthenticationToken authentication, Principal principal) {
        // Handle Google OAuth2 callback
        return "Welcome, " + principal.getName();
    }
}
