package com.cookbook.api.services.impl;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cookbook.api.models.UserEntity;

import com.cookbook.api.security.SecretKeyGenerator;
import com.cookbook.api.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

//    private final Key SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    private final SecretKeyGenerator secretKeyGenerator;


    @Override
    public String generateToken(UserEntity userEntity) {
        Date now = new Date();
        Date validTil = new Date(now.getTime() * 3600000); //1hr

        Algorithm algorithm = Algorithm.HMAC256(secretKeyGenerator.secretKey);

        return JWT.create()
                .withSubject(userEntity.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(validTil)
                .sign(algorithm);
    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKeyGenerator.secretKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();

        DecodedJWT decodedJWT = jwtVerifier.verify(token);

        UserDto userDto =
    }
}
