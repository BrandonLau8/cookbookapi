package com.cookbook.api.security;

import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.UserRepository;
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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        daoAuthProvider.setUserDetailsService(userDetailsService); // Set the UserDetailsService manually
        daoAuthProvider.setPasswordEncoder(passwordEncoder); // Set the PasswordEncoder manually
    }

    @Test
    public void testAuthenticate_Success() {
        // Given
        Integer id = 1;
        String username = "testUser";
        String password = "testPassword";
        String encodedPassword = "encodedPassword";
        Set<GrantedAuthority> roles = Collections.singleton("USER");
        // Mock UserDetails
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);
        when(userDetails.getPassword()).thenReturn(encodedPassword); // Mock password retrieval
        when(userDetails.getAuthorities()).thenReturn(roles);

        // Mock UserEntity
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getUsername()).thenReturn(username);
        when(userEntity.getPassword()).thenReturn(encodedPassword);

        // Mock Authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, roles);



        // Mock behavior

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

//        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        when(userDetails.isAccountNonLocked()).thenReturn(true); // Simulate unlocked account
        when(userDetails.isEnabled()).thenReturn(true); // Simulate enabled account
        when(userDetails.isAccountNonExpired()).thenReturn(true);
        when(userDetails.isCredentialsNonExpired()).thenReturn(true);

        // When
        Authentication result = daoAuthProvider.authenticate(authentication);;

        // Then
        assertNotNull(result); // Verify that authentication result is not null
        assertTrue(result.isAuthenticated()); // Verify that authentication was successful
        verify(passwordEncoder).encode(password);
        verify(userDetailsService).loadUserByUsername(username);
    }
}