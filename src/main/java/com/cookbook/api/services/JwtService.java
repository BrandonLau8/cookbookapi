package com.cookbook.api.services;

import com.auth0.jwt.algorithms.Algorithm;
import com.cookbook.api.dto.UserDto;
import com.cookbook.api.exceptions.LoginException;
import com.cookbook.api.models.UserEntity;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;


public interface JwtService {

    String generateToken(String username);

    Authentication validateToken(String token);

    void invalidateToken(HttpServletRequest request);

//    String extractUsername(String token);
}
