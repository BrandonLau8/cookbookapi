package com.cookbook.api.security;

import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor
class DaoAuthProviderTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DaoAuthProvider daoAuthProvider;

    @BeforeEach
   void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
   void testAuthenticate_Success() {
        // Given
        String username = "testUser";
        String rawPassword = "testPassword";
        String encodedPassword = "testPassword";
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);
        when(userDetails.getPassword()).thenReturn(encodedPassword);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Mock Authentication
        Authentication authentication = mock(UsernamePasswordAuthenticationToken.class);
        when(authentication.getName()).thenReturn(username);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authentication.getCredentials()).thenReturn(rawPassword);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        // When
        Authentication result = daoAuthProvider.authenticate(authentication);

        // Then
        assertNotNull(result); // Verify that authentication result is not null
        assertTrue(result.isAuthenticated()); // Verify that authentication was successful
        assertEquals(userDetails, result.getPrincipal());
        assertEquals(rawPassword, result.getCredentials());
        verify(userDetailsService).loadUserByUsername(username);
    }
}