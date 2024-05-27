package com.cookbook.api.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cookbook.api.models.InvalidToken;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.security.SecretKeyGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class JwtUtil {

    private final SecretKeyGenerator secretKeyGenerator;

    public JwtUtil(SecretKeyGenerator secretKeyGenerator) {
        this.secretKeyGenerator = secretKeyGenerator;
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

    public DecodedJWT verifyToken(String token) {
        //encrypt the secret key
        Algorithm algorithm = Algorithm.HMAC256(secretKeyGenerator.getSecretKey());

        //Uses the encrypted secret key to verify the token
        JWTVerifier verifier = JWT.require(algorithm).build();

        //Verifies the token with encrypted key returning a decoded token
        return verifier.verify(token);
    }

    public String extractTokenFromCookies(HttpServletRequest request, String token) throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (token.equals(cookie.getName())) {
                    return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                }
            }
        }
        return null;
    }
}