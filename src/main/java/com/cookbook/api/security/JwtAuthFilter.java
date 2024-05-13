package com.cookbook.api.security;

import com.cookbook.api.dto.UserDto;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.services.AuthService;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Data
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final SecretKeyGenerator secretKeyGenerator;

    @Autowired
    public JwtAuthFilter(JwtService jwtService, SecretKeyGenerator secretKeyGenerator) {
        this.jwtService = jwtService;
        this.secretKeyGenerator = secretKeyGenerator;
    }

    //Filter intercepts incoming requests
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        //Get the Request Authorization Header
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        //Split Bearer from token from Authorization Header
        if (header != null) {
            String[] authElements = header.split(" ");

            //Check if Header was properly split
            if (authElements.length == 2 && "Bearer".equals(authElements[0])) {
                try {
                    //Validate by checking if username exists and token is not expired
                    Authentication authentication = jwtService.validateToken(authElements[1]);

                    //Retrieve the Context from Context Holder and sets the Validated Authentication Token to Authentication object
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } catch (RuntimeException e) {
                    //Clear Context
                    SecurityContextHolder.clearContext();
                    throw e;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}