package com.cookbook.api.services.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cookbook.api.security.SecretKeyGenerator;
import com.cookbook.api.services.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        String token = jwtService.createAccessToken(username);

        // Assert
        assertNotNull(token);
        // Additional assertions can be added based on token structure or properties
    }

//    @Test
//    void validateToken_ValidToken() {
//        //Arrange
//        String username = "testUser";
//        String secretKey = "your_predefined_secret_key"; // Provide a predefined secret key
//        doReturn(secretKey).when(secretKeyGenerator).getSecretKey();
////        when(secretKeyGenerator.getSecretKey()).thenReturn(secretKey);
//        DecodedJWT decodedJWT = mock(DecodedJWT.class);
//        when(decodedJWT.getSubject()).thenReturn(username);
//
//        String token = jwtService.generateToken(username);
//        UserDto userDto = new UserDto(
//                1, username, token, null);
//
//        when(userService.findByUsername(decodedJWT.getSubject())).thenReturn(userDto);
//
//
//    }

//    @Test
//    void verifyToken_ValidToken() {
//        //Arrange
//        String username = "testUser";
//        String secretKey = "your_predefined_secret_key"; // Provide a predefined secret key
//        when(secretKeyGenerator.getSecretKey()).thenReturn(secretKey);
//
//        //Act
//        String token = jwtService.createAccessToken(username);
//        DecodedJWT result = jwtService.verifyToken(token);
//
//        //Assert
//        assertNotNull(result);
//        assertEquals(username, result.getSubject());
//    }
}