package com.cookbook.api.controllers;


import com.cookbook.api.dto.LoginDto;
import com.cookbook.api.dto.RegisterDto;
import com.cookbook.api.dto.UserDto;

import com.cookbook.api.services.AuthService;
import com.cookbook.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class AuthController {


    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterDto registerDto) {
        UserDto createdUser = authService.register(registerDto);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }
}





