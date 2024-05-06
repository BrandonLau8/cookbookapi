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
import java.util.Date;

@RequiredArgsConstructor
@Data
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final SecretKeyGenerator secretKeyGenerator;


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

                } catch (RuntimeException e) {
                    SecurityContextHolder.clearContext();
                    throw e;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    public static Date extractExpiration(String token, String secretKey) {
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey)
                        .requireIssuer("http://localhost:8080/auth").build().parseClaimsJws(token);
        return claimsJws.getBody().getExpiration();
    }

//    private static boolean isTokenExpired(String token, String secretKey) {
//        Date expiration = extractExpiration(token, secretKey);
//        return expiration.before(new Date());
//    };

//    public static Token extractTokenFromAuthentication(Authentication authentication) {
//        if (authentication instanceof UsernamePasswordAuthenticationToken) {
//            UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
//            UserDto userDto = (UserDto) authenticationToken.getPrincipal();
//
//            Token token = new Token();
//            token.setToken(userDto.getToken());
//
//            return token;
//        }
//        return null;
//    }
//
//    private void handleTokenExpiration(Authentication authentication) {
//        //Update Token status
//        Token expiredToken = extractTokenFromAuthentication(authentication);
//        expiredToken.setStatus(false);
//        tokenRepository.save(expiredToken);
//
//        UserEntity userEntity = expiredToken.getPerson();
//        userEntity.setStatus(false);
//    }
}