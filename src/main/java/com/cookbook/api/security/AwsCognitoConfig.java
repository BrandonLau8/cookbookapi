package com.cookbook.api.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class AwsCognitoConfig {
    @Value("${spring.security.oauth2.client.registration.cognito.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.cognito.logout-uri}")
    private String logoutUri;

    @Value("${spring.aws.cognito.logout-url}")
    private String logoutUrl;

    public String getLogoutUrl() {
        return String.format("%s?client_id=%s&logout_uri=%s", logoutUrl, clientId, logoutUri);
    }
}
