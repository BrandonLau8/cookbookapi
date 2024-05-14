package com.cookbook.api.services.impl;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.cookbook.api.models.InvalidToken;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.InvalidTokenRepository;
import com.cookbook.api.repository.UserRepository;
import com.cookbook.api.security.SecretKeyGenerator;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;


@Data
@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKeyGenerator secretKeyGenerator;

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final InvalidTokenRepository invalidTokenRepository;

    private final UserRepository userRepository;

    @Autowired
    public JwtServiceImpl(SecretKeyGenerator secretKeyGenerator, UserService userService, UserDetailsService userDetailsService, InvalidTokenRepository invalidTokenRepository, UserRepository userRepository) {
        this.secretKeyGenerator = secretKeyGenerator;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.invalidTokenRepository = invalidTokenRepository;
        this.userRepository = userRepository;
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

            //Check if token exists in blacklist
            if (invalidTokenRepository.findByToken(token).isPresent()) {
                throw new AuthenticationServiceException("Invalid token");
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

    @Override
    @Transactional
    public void invalidateToken(HttpServletRequest request) {
        //Get token from request header
        String token = extractTokenFromHeader(request);

        //Verify token returning a decoded token
        DecodedJWT decodedJWT = verifyToken(token);

        //Find user from decoded token
        UserDetails userDetails = userDetailsService.loadUserByUsername(decodedJWT.getSubject());
        UserEntity userEntity = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()->{throw new UsernameNotFoundException("Username not found");});

        // Check if the token exists in the blacklist
        Optional<InvalidToken> invalidTokenOptional = invalidTokenRepository.findByToken(token);
        if (invalidTokenOptional.isPresent()) {
            throw new AuthenticationServiceException("Token is already invalid");
        }


        //Create invalid token
        InvalidToken invalidToken = new InvalidToken().builder()
                .token(token)
                .person(userEntity)
                .validity(false)
                .build();

        //Save invalidToken
        invalidTokenRepository.save(invalidToken);
    }

    DecodedJWT verifyToken(String token) {
        //encrypt the secret key
        Algorithm algorithm = Algorithm.HMAC256(secretKeyGenerator.getSecretKey());

        //Uses the encrypted secret key to verify the token
        JWTVerifier verifier = JWT.require(algorithm).build();

        //Verifies the token with encrypted key returning a decoded token
        return verifier.verify(token);
    }

    String extractTokenFromHeader(HttpServletRequest request) {
        //Get the Request Authorization Header
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        //Split Bearer from token from Authorization Header
        if (header != null) {
            String[] authElements = header.split(" ");

            //Check if Header was properly split
            if (authElements.length == 2 && "Bearer".equals(authElements[0])) {
                return authElements[1];
            }
        }
        throw new RuntimeException("Invalid or missing Request Header");
        }
    }
