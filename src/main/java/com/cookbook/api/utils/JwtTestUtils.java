package com.cookbook.api.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;
import java.util.Date;

@RequiredArgsConstructor
@Data
public class JwtTestUtils {
    // Hardcoded secret key for testing
    public static final SecretKey SECRET_KEY =  Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Generate a sample JWT token for testing
    public static String generateSampleToken() {
        String sampleToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Expires in 1 hour
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        return sampleToken;
    }
}
