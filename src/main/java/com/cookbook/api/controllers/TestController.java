package com.cookbook.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/auth/login")
    public ResponseEntity<String> login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        return new ResponseEntity<>("i am logged in", HttpStatus.OK);
    }

//    @GetMapping("/auth/user")
//   public ResponseEntity<String> user() {
//        return new ResponseEntity<>("i am user", HttpStatus.OK);
//    }

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
