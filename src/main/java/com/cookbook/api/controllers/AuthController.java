package com.cookbook.api.controllers;


import com.cookbook.api.dto.LoginDto;
import com.cookbook.api.dto.RegisterDto;
import com.cookbook.api.dto.UserDto;

import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.UserRepository;
import com.cookbook.api.services.AuthService;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final UserService userService;

    @Autowired
    private final AuthService authService;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterDto registerDto) {
        UserDto createdUser = authService.register(registerDto);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response, HttpServletRequest request) {
        UserDto userDto = authService.login(loginDto);
        String token = jwtService.generateToken(userDto.getUsername());

        // Add the token to an HTTP-only cookie
        Cookie tokenCookie = new Cookie("token", token);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");

        // Add more cookie attributes as needed (secure, SameSite, etc.)
         tokenCookie.setSecure(true); // Uncomment if using HTTPS
        // tokenCookie.setSameSite(SameSite.NONE.toString()); // Adjust as needed

        response.addCookie(tokenCookie);
//        userDto.setToken();

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

//    @GetMapping("/status")
//    public ResponseEntity<Boolean> getTokenStatus(LoginDto loginDto) {
//        UserEntity userEntity = userRepository.findByUsername(loginDto.getUsername()).orElse(null);
//        if (userEntity == null) {
//            return ResponseEntity.notFound().build();
//        }
//        // Check if any of the user's tokens are marked as logged out
//        UserDto userDto = new UserDto();;
//        userDto.setLoggedOut(userEntity.isLoggedOut());
//        boolean isUserLoggedOut = userDto.isLoggedOut();
//        return ResponseEntity.ok(isUserLoggedOut);
//    }
}






