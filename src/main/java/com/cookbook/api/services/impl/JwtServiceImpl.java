package com.cookbook.api.services.impl;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.cookbook.api.models.InvalidToken;
import com.cookbook.api.models.RefreshToken;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.InvalidTokenRepository;
import com.cookbook.api.repository.RefreshTokenRepository;
import com.cookbook.api.repository.UserRepository;
import com.cookbook.api.security.SecretKeyGenerator;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import com.cookbook.api.utils.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Data
@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKeyGenerator secretKeyGenerator;

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final InvalidTokenRepository invalidTokenRepository;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public JwtServiceImpl(SecretKeyGenerator secretKeyGenerator, UserService userService, UserDetailsService userDetailsService, InvalidTokenRepository invalidTokenRepository, UserRepository userRepository, JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.secretKeyGenerator = secretKeyGenerator;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.invalidTokenRepository = invalidTokenRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public String createAccessToken(String username) {
        Date now = new Date();
        Date validTil = new Date(now.getTime() * 3600000);

        Algorithm algorithm = Algorithm.HMAC256(secretKeyGenerator.getSecretKey());

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(validTil)
                .sign(algorithm);
    }

    @Override
    public RefreshToken createRefeshToken(String username) {

        RefreshToken refreshToken = RefreshToken.builder()
                .person(userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Username not found")))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000)) //10mins
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Authentication validateToken(String token) {

        try {
            //Verifies token returning a decoded token
            DecodedJWT decodedJWT = jwtUtil.verifyToken(token);

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
    public RefreshToken verifyExpiration(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(()->new RuntimeException("RefreshToken not found"));
        if(refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException(refreshToken.getToken() + "Refresh token expired. Please make a new signin request");
        }
        return refreshToken;
    }

    @Override
    public void invalidateToken(String token) {

        //Verify token returning a decoded token
        DecodedJWT decodedJWT = jwtUtil.verifyToken(token);

        //Find user from decoded token
        UserDetails userDetails = userDetailsService.loadUserByUsername(decodedJWT.getSubject());
        UserEntity userEntity = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("Username not found");
                });

        // Check if the token exists in the blacklist
        Optional<InvalidToken> invalidTokenOptional = invalidTokenRepository.findByToken(token);
        if (invalidTokenOptional.isPresent()) {
            throw new AuthenticationServiceException("Token is already invalid");
        }

        // Find the refresh token by its token value
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(token);
        //Delete token from Refresh Token DAO
       if(refreshTokenOptional.isPresent()) {
           refreshTokenRepository.delete(refreshTokenOptional.get());
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
}





