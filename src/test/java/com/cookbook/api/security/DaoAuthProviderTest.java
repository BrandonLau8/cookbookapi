package com.cookbook.api.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DaoAuthProviderTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DaoAuthProvider daoAuthProvider;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        daoAuthProvider.setUserDetailsService(userDetailsService); // Set the UserDetailsService manually
        daoAuthProvider.setPasswordEncoder(passwordEncoder); // Set the PasswordEncoder manually
    }

    @Test
    public void testAuthenticate_Success() {
        // Given
        String username = "testUser";
        String password = "testPassword";
        String encodedPassword = "encodedPassword";
        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        // Mock behavior
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(userDetails.isAccountNonLocked()).thenReturn(true); // Simulate unlocked account
        when(userDetails.isEnabled()).thenReturn(true); // Simulate enabled account
        when(userDetails.isAccountNonExpired()).thenReturn(true);
        when(userDetails.isCredentialsNonExpired()).thenReturn(true);

        // When
        Authentication result = daoAuthProvider.authenticate(authentication);

        // Then
        assertNotNull(result); // Verify that authentication result is not null
        assertTrue(result.isAuthenticated()); // Verify that authentication was successful
        verify(passwordEncoder).encode(password);
        verify(userDetailsService).loadUserByUsername(username);
    }
}