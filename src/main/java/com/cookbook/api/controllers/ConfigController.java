package com.cookbook.api.controllers;

import com.cookbook.api.security.AwsCognitoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ConfigController {
    private final AwsCognitoConfig awsCognitoConfig;

    @Autowired
    public ConfigController(AwsCognitoConfig awsCognitoConfig) {
        this.awsCognitoConfig = awsCognitoConfig;
    }

    @GetMapping("/api/config")
    public Map<String, String> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("logoutUrl", awsCognitoConfig.getLogoutUrl());
        return config;
    }
}
