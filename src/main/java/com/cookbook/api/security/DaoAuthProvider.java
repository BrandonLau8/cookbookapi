package com.cookbook.api.security;

import com.cookbook.api.services.JwtService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DaoAuthProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsService;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public DaoAuthProvider(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    //Auth Provider checks if the username and password are correct
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //Get Input Username from unauthenticated Object (loginDto)
        String username = authentication.getName();

        //Check if UserDetails exists
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        //Get Input Password from unathenticated Object (loginDto)
        String inputPassword = authentication.getCredentials().toString();

        //Get Encoded Password from UserDetails (Database)
        String encodedPassword = userDetails.getPassword();
//        Set<GrantedAuthority> roles = authentication.getAuthorities().stream()
//                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
//                .collect(Collectors.toSet());

        //Check if Passwords match each other by encoding inputPassword and comparing
        if (passwordEncoder.matches(inputPassword, encodedPassword)) {

            //Create authenticated token and set into SecurityContext
            Authentication authenticated = new UsernamePasswordAuthenticationToken(userDetails, inputPassword, null);
//            SecurityContextHolder.getContext().setAuthentication(authenticated);
            return authenticated;
        } else {
            throw new AuthenticationException("Invalid credentials") {}; // You can customize the exception message here
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
