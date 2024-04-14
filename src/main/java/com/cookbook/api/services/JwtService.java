package com.cookbook.api.services;

import com.cookbook.api.models.UserEntity;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.function.Function;

public interface JwtService {
    public String extractUsername(String token);

    public boolean isValid(String token, UserEntity userEntity);

    private boolean isTokenExpired(String token) {
        return false;
    }

    private Date extractExpiration(String token) {
        return null;
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver);

    public String generateToken(UserEntity userEntity);


}
