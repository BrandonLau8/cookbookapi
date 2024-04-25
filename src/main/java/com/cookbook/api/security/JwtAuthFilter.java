package com.cookbook.api.security;

import com.cookbook.api.models.UserEntity;
import com.cookbook.api.services.AuthService;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Data
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    //Filter intercepts incoming requests
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null) {
            String[] authElements = header.split(" ");

            if (authElements.length == 2 && "Bearer".equals(authElements[0])) {
                try {
                    //Validate and possibly refresh token
                    Authentication authentication = jwtService.validateToken(authElements[1]);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    //Optionally check if token is expired
                    if(authentication != null && isTokenExpired(authentication))
                } catch (RuntimeException e) {
                    SecurityContextHolder.clearContext();
                    throw e;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenExpired(Authentication authentication) {
        // Extract expiration time from token and check if it's expired
        authentication.getPrincipal().
    };
}