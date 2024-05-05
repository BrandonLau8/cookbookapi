//package com.cookbook.api.security;
//
//import com.cookbook.api.dto.UserDto;
//import com.cookbook.api.mappers.UserMappers;
//import com.cookbook.api.models.UserEntity;
//import com.cookbook.api.repository.UserRepository;
//import com.cookbook.api.services.JwtService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthProvider implements AuthenticationProvider {
//
//    private final JwtService jwtService;
//
//    private final UserRepository userRepository;
//
//    private final UserMappers userMappers;
//
//    private final UserDetailsService userDetailsService;
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String jwt = jwtService.generateToken(username);
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//        UserEntity userEntity = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
//
//        UserDto userDto = userMappers.maptoDto(userEntity);
//        userDto.setToken(jwt);
//
//        return new UsernamePasswordAuthenticationToken(userDetails, null);
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return false;
//    }
//}
