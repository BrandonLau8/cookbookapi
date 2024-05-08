package com.cookbook.api.services.impl;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cookbook.api.dto.UserDto;

import com.cookbook.api.security.SecretKeyGenerator;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;


@Data
@Service
public class JwtServiceImpl implements JwtService {

//    private final Key SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    private final SecretKeyGenerator secretKeyGenerator;

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtServiceImpl(SecretKeyGenerator secretKeyGenerator, UserService userService, UserDetailsService userDetailsService) {
        this.secretKeyGenerator = secretKeyGenerator;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String generateToken(String username) {
        Date now = new Date();
        Date validTil = new Date(now.getTime() * 3600000); //1hr

        Algorithm algorithm = Algorithm.HMAC256(secretKeyGenerator.getSecretKey());

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(validTil)
                .sign(algorithm);
    }

    @Override
    public Authentication validateToken(String token) {

        DecodedJWT decodedJWT = verifyToken(token);

        String username = decodedJWT.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
    }

    DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKeyGenerator.getSecretKey());
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        return verifier.verify(token);
    }


}
