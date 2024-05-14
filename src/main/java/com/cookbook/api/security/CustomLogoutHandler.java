package com.cookbook.api.security;

import com.cookbook.api.dto.UserDto;

import com.cookbook.api.models.UserEntity;

import com.cookbook.api.repository.UserRepository;
import com.cookbook.api.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Data
@Configuration
public class CustomLogoutHandler implements LogoutHandler {

    private final AuthService authService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        authService.logout(request);
        try {
            if (!response.isCommitted()) {

                response.getWriter().write("Logout successful");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }

