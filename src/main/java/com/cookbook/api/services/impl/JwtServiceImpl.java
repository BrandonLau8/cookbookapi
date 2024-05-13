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
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.function.Function;


@Data
@Service
public class JwtServiceImpl implements JwtService {

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

        try {

            //Verifies token returning a decoded token
            DecodedJWT decodedJWT = verifyToken(token);

            //Check if the username exists
            String username = decodedJWT.getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);


            //Check if token is not expired
            if (decodedJWT.getExpiresAt().before(new Date())) {
                throw new JWTVerificationException("Token is expired");
            }

            //Return the authenticated object
            return new UsernamePasswordAuthenticationToken(userDetails, token, Collections.emptyList());

        } catch (JWTVerificationException e) {
            //Token Verification failed
            throw new AuthenticationServiceException("Token verification failed", e);
        } catch (UsernameNotFoundException e) {
            //Username not found
            throw new UsernameNotFoundException("User not found");
        }

    }

    DecodedJWT verifyToken(String token) {
        //encrypt the secret key
        Algorithm algorithm = Algorithm.HMAC256(secretKeyGenerator.getSecretKey());

        //Uses the encrypted secret key to verify the token
        JWTVerifier verifier = JWT.require(algorithm).build();

        //Verifies the token with encrypted key returning a decoded token
        return verifier.verify(token);
    }

}
