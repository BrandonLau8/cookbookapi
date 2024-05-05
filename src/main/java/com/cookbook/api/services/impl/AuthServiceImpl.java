package com.cookbook.api.services.impl;

import com.cookbook.api.dto.LoginDto;
import com.cookbook.api.dto.RegisterDto;
import com.cookbook.api.dto.UserDto;
import com.cookbook.api.exceptions.LoginException;
import com.cookbook.api.mappers.UserMappers;
import com.cookbook.api.models.RoleEntity;
import com.cookbook.api.models.RoleType;
import com.cookbook.api.models.Token;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.RoleRepository;
import com.cookbook.api.repository.TokenRepository;
import com.cookbook.api.repository.UserRepository;
import com.cookbook.api.security.DaoAuthProvider;
import com.cookbook.api.security.PasswordConfig;
import com.cookbook.api.services.AuthService;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Data
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final TokenRepository tokenRepository;

    private final PasswordConfig passwordConfig;

    private final UserService userService;

    private final JwtService jwtService;

    private final UserMappers userMappers;

    private final UserDetailsService userDetailsService;

    private final DaoAuthProvider daoAuthProvider;


//    @Override
//    public UserDto login(LoginDto loginDto) {
//        String username = loginDto.getUsername();
//
//        UserEntity userEntity = userService.loadUserByUsername(username);
//        if (passwordConfig.passwordEncoder().matches(loginDto.getPassword(), userEntity.getPassword())) {
//            return maptoDto(userEntity);
//        }
//        throw new LoginException("Invalid Password", HttpStatus.BAD_REQUEST);
//    }

    @Override
    public UserDto login(LoginDto loginDto) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authenticated = daoAuthProvider.authenticate(authentication);

        // If authentication is successful, generate a token or return user information
        UserDetails userDetails = (UserDetails) authenticated.getPrincipal();
        String jwt = jwtService.generateToken(userDetails.getUsername());
        UserDto userDto = userMappers.detailToDto(userDetails);
        userDto.setToken(jwt);

        return userDto;
    }

    @Override
    public UserDto register(RegisterDto registerDto) {
        userRepository.findByUsername(registerDto.getUsername())
                .ifPresent((userEntity) ->
                {throw new LoginException("User already exists", HttpStatus.BAD_REQUEST);
                });

        UserEntity userEntity = userMappers.maptoEntity(registerDto);
        userRepository.save(userEntity);

        return userMappers.maptoDto(userEntity);
    }





//    private List<Token> saveTokens(String jwt, UserEntity userEntity) {
//        //Set new JWT to Token and Status to true
//        Token token = new Token();
//        token.setToken(jwt);
//        token.setStatus(false);
//        tokenRepository.save(token);
//
//        //Save Token to UserEntity
//        List<Token> savedTokens = new ArrayList<>();
//        savedTokens.add(token);
//        List<String> tokenStrings = savedTokens.stream()
//                .map(Token::getToken)
//                .collect(Collectors.toList());
//        userEntity.setTokens(tokenStrings);
//
//        return savedTokens;
//    }

//    private void revokeAllTokensByUser(UserEntity userEntity) {
//        //Find all Tokens related to UserEntity
//        List<Token> validTokens = tokenRepository.findAllTokensByPersonId(userEntity.getId());
//        if(validTokens.isEmpty()) {
//            return;
//        }
//        //Set Status of Tokens related to UserEntity to false
//        validTokens.forEach(t-> {
//            t.setStatus(false);
//        });
//
//        tokenRepository.saveAll(validTokens);
//    }

}
