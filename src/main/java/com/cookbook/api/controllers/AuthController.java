package com.cookbook.api.controllers;


import com.cookbook.api.dto.*;

import com.cookbook.api.models.RefreshToken;
import com.cookbook.api.services.AuthService;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@RestController
public class AuthController {

    private final AuthService authService;
    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;

    private final JwtUtil jwtUtil;



    @Autowired
    public AuthController(AuthService authService, UserDetailsService userDetailsService, JwtService jwtService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;

        this.jwtService = jwtService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterDto registerDto) {
        UserDto createdUser = authService.register(registerDto);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {
        UserDto loggingUser = authService.login(loginDto); //Returns userDetails
        String accessToken = jwtService.createAccessToken(loggingUser.getUsername());

        //Encode to avoide illegal chars note: jwt tokens are already encode with base64
        String encodeAccessToken = URLEncoder.encode(accessToken, StandardCharsets.UTF_8);

        Cookie accessCookie = new Cookie("Authorization", encodeAccessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true); //Ensure set true in prod
        accessCookie.setPath("/");
        accessCookie.setMaxAge(3600);
        response.addCookie(accessCookie);

        //Create refreshToken and encode to store into cookie
        RefreshToken refreshToken = jwtService.createRefeshToken(loggingUser.getUsername());
        String encodeRefreshToken = URLEncoder.encode(refreshToken.getToken(), StandardCharsets.UTF_8);

        //Store into cookie
        Cookie refreshCookie = new Cookie("Refresh", encodeRefreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true); //Ensure set true in prod
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(3600);
        response.addCookie(refreshCookie);


        return new ResponseEntity<>(loggingUser, HttpStatus.OK);
    }

    @PostMapping("/refreshlogin")
    public ResponseEntity<?> refreshLogin(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //Find Refresh Cookie in Cookies Array
        String refreshToken = jwtUtil.extractTokenFromCookies(request, "Refresh");

        //Validate RefreshToken
        RefreshToken validRefreshToken = jwtService.verifyExpiration(refreshToken);

        try {
            String newAccessToken = jwtService.createAccessToken(validRefreshToken.getPerson().getUsername());
            String encodedAccessToken = URLEncoder.encode(newAccessToken, StandardCharsets.UTF_8);

            Cookie newAccessTokenCookie = new Cookie("Authorization", encodedAccessToken);
            newAccessTokenCookie.setHttpOnly(true);
            newAccessTokenCookie.setSecure(true); // Ensure set to true in prod
            newAccessTokenCookie.setPath("/");
            newAccessTokenCookie.setMaxAge(3600); // 1 hour expiration
            response.addCookie(newAccessTokenCookie);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }


    @PostMapping("/logout")
    public String logout(HttpServletRequest request) throws IOException {
        //Access Token is deleted by security config

        //Get RefreshToken
        String refreshToken = jwtUtil.extractTokenFromCookies(request, "Refresh");
        String accessToken = jwtUtil.extractTokenFromCookies(request, "Authorization");

        //Invalidate RefreshToken
        jwtService.invalidateToken(refreshToken);
        jwtService.invalidateToken(accessToken);
        return ("Tokens Invalidated");
    }

    @GetMapping("/")
    public String logoutGet() {
        return ("Logout successful");
    }

    @GetMapping("/test")
    public String test() {return ("Test");}

}






