package com.cookbook.api.controllers;


import com.cookbook.api.dto.*;

import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.UserRepository;
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
import java.util.HashMap;
import java.util.Map;


@RestController
public class AuthController {

    private final AuthService authService;
    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;


    @Autowired
    public AuthController(AuthService authService, UserDetailsService userDetailsService, JwtService jwtService) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;

        this.jwtService = jwtService;
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
        String accessToken = jwtService.generateToken(loggingUser.getUsername());
        response.setHeader("Authorization", "Bearer " + accessToken);
        System.out.println(response.getHeader("Authorization"));
        return new ResponseEntity<>(loggingUser, HttpStatus.OK);
    }

    @PostMapping("/refreshlogin")
    public ResponseEntity<UserDto> refreshLogin(@RequestBody RefreshTokenDto refreshTokenDto) {
        UserDto refreshUser = authService.refreshLogin(refreshTokenDto);
        return new ResponseEntity<>(refreshUser, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.logout(request);
        return ("Logout successful");
    }
}






