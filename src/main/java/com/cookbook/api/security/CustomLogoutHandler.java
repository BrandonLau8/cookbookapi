package com.cookbook.api.security;

import com.cookbook.api.dto.UserDto;
import com.cookbook.api.models.Token;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.TokenRepository;
import com.cookbook.api.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.Optional;

@RequiredArgsConstructor
@Data
@Configuration
public class CustomLogoutHandler implements LogoutHandler {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);
        Token storedToken = tokenRepository.findByToken(token).orElse(null);


        if(storedToken != null) {
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);

            if(authentication != null && authentication.getPrincipal() instanceof UserDto) {
                UserDto userDto = (UserDto) authentication.getPrincipal();
                UserEntity userEntity = userRepository.findByUsername(userDto.getUsername()).orElseThrow();

                if(userEntity != null) {
                    userEntity.setLoggedOut(true);
                    userRepository.save(userEntity);
                }
            }
            Cookie cookie = new Cookie("Authorization", null);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }



    }
}
