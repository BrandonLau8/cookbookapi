package com.cookbook.api.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.cookbook.api.dto.UserDto;
import com.cookbook.api.security.SecretKeyGenerator;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import com.cookbook.api.utils.JwtTestUtils;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.XbPfbIHMI6arZ3Y922BhjWgQzWXcXNrz0ogtVhfEd2o";
        String secretKey = "secret"; // Provide a predefined secret key
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");

        when(secretKeyGenerator.getSecretKey()).thenReturn(secretKey);
        when(jwtService.verifyToken(token)).thenReturn(decodedJWT);
        when(decodedJWT.getSubject()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(userDto);

        //Act
        Authentication authentication = jwtService.validateToken(token);

        //Assert
        assertNotNull(authentication);
        assertInstanceOf(UserDto.class, authentication.getPrincipal());
        assertEquals("testUser", (((UserDto) authentication.getPrincipal()).getUsername()));

    }

    @Test
    void verifyToken_ValidToken() {
        //Arrange
        String token = JwtTestUtils.generateSampleToken();

        when(secretKeyGenerator.getSecretKey()).thenReturn(Base64.getEncoder().encodeToString(JwtTestUtils.SECRET_KEY.getEncoded()));


        //Act
        DecodedJWT result = jwtService.verifyToken(token);

        //Assert
        assertNotNull(result);
        assertEquals("testUser", result);
    }
}