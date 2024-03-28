package com.cookbook.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @GetMapping("/login")
   public String login() {
        return "login";
    }

//    @GetMapping("/")
//    public String home() {
//        return("<h1>Welcome</h1>");
//    }
//
//    @GetMapping("/user")
//    public String user() {
//        return("<h1>User</h1>");
//    }
//
//    @GetMapping("/admin")
//    public String admin() {
//        return("<h1>Admin</h1>");
//    }
}
