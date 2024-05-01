package com.cookbook.api.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.cookbook.api.dto.UserDto;
import com.cookbook.api.models.Token;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.security.SecretKeyGenerator;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import com.cookbook.api.utils.JwtTestUtils;
import io.jsonwebtoken.Jwt;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor
@Data
class JwtServiceImplTest {

    @Mock
    private SecretKeyGenerator secretKeyGenerator;

    @Mock
    private UserService userService;

    @InjectMocks
    private JwtServiceImpl jwtService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void generateToken() {
        // Arrange
        String username = "testUser";
        String secretKey = "your_predefined_secret_key"; // Provide a predefined secret key
        when(secretKeyGenerator.getSecretKey()).thenReturn(secretKey);

        // Act
        String token = jwtService.generateToken(username);

        // Assert
        assertNotNull(token);
        // Additional assertions can be added based on token structure or properties
    }

    @Test
    void validateToken_ValidToken() {
        //Arrange
        String username = "testUser";
        String secretKey = "your_predefined_secret_key"; // Provide a predefined secret key
        doReturn(secretKey).when(secretKeyGenerator).getSecretKey();
//        when(secretKeyGenerator.getSecretKey()).thenReturn(secretKey);
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getSubject()).thenReturn(username);

        String token = jwtService.generateToken(username);
        UserDto userDto = new UserDto(
                1, "brandon", "lau", username, token, true
        );

        when(userService.findByUsername(decodedJWT.getSubject())).thenReturn(userDto);

        //Act
        Authentication authentication = jwtService.validateToken(token);

        //Assert
        assertNotNull(authentication);
        assertEquals(userDto, authentication.getPrincipal());
    }

    @Test
    void verifyToken_ValidToken() {
        //Arrange
        String username = "testUser";
        String secretKey = "your_predefined_secret_key"; // Provide a predefined secret key
        when(secretKeyGenerator.getSecretKey()).thenReturn(secretKey);

        //Act
        String token = jwtService.generateToken(username);
        DecodedJWT result = jwtService.verifyToken(token);

        //Assert
        assertNotNull(result);
        assertEquals(username, result.getSubject());
    }
}