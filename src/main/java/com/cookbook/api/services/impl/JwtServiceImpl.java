package com.cookbook.api.services.impl;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cookbook.api.dto.UserDto;

import com.cookbook.api.security.SecretKeyGenerator;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
@Data
@Service
public class JwtServiceImpl implements JwtService {

//    private final Key SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    private final SecretKeyGenerator secretKeyGenerator;

    private final UserService userService;


    @Override
    public String generateToken(String username) {
        Date now = new Date();
        Date validTil = new Date(now.getTime() * 3600000); //1hr

        Algorithm algorithm = Algorithm.HMAC256(secretKeyGenerator.secretKey);

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(validTil)
                .sign(algorithm);
    }

    @Override
    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKeyGenerator.secretKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();

        DecodedJWT decodedJWT = jwtVerifier.verify(token);

        UserDto userDto = userService.findByUsername(decodedJWT.getSubject());

        return new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList());
    }


}
