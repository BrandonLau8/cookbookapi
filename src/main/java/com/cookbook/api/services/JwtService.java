package com.cookbook.api.services;

import com.cookbook.api.models.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;


public interface JwtService {

    String createAccessToken(String username);

    RefreshToken createRefeshToken(String username);

    Authentication validateToken(String token);

    RefreshToken verifyExpiration(String token);

    void invalidateToken(String token);

//    String extractUsername(String token);
}
