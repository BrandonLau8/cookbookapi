package com.cookbook.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    private final AuthenticationManager authenticationManager;

    public TestController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Authentication> login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);
        // ...
        return new ResponseEntity<>(authenticationRequest, HttpStatus.OK);
    }

    public record LoginRequest(String username, String password) {
    }


//    @PostMapping("/auth/login")
//    public ResponseEntity<String> login(String username, String password) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(username, password)
//        );
//        return new ResponseEntity<>("i am logged in", HttpStatus.OK);
//    }

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
