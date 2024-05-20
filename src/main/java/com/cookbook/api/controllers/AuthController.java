package com.cookbook.api.controllers;


import com.cookbook.api.dto.*;

import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.UserRepository;
import com.cookbook.api.security.AwsCognitoConfig;
import com.cookbook.api.services.AuthService;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;


@RestController
public class AuthController {

    private final AuthService authService;
    private final UserDetailsService userDetailsService;

    private final AwsCognitoConfig awsCognitoConfig;

    @Autowired
    public AuthController(AuthService authService, UserDetailsService userDetailsService, AwsCognitoConfig awsCognitoConfig) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;
        this.awsCognitoConfig = awsCognitoConfig;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterDto registerDto) {
        UserDto createdUser = authService.register(registerDto);
//        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response, HttpServletRequest request) {
        UserDto loggingUser = authService.login(loginDto);
        return new ResponseEntity<>(loggingUser, HttpStatus.OK);
    }

    @PostMapping("/refreshlogin")
    public ResponseEntity<UserDto> refreshLogin(@RequestBody RefreshTokenDto refreshTokenDto) {
        UserDto refreshUser = authService.refreshLogin(refreshTokenDto);
        return new ResponseEntity<>(refreshUser, HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        return "hello";
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Clear local cookies or session storage if any
        request.getSession().invalidate();

        //Redirect to AWS Cognito logout URL
        response.sendRedirect(awsCognitoConfig.getLogoutUrl());

    }
}






