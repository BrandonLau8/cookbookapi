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
import com.cookbook.api.security.PasswordConfig;
import com.cookbook.api.services.AuthService;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
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

        //Look for User
        UserEntity userEntity = userRepository.findByUsername(loginDto.getUsername()).orElseThrow();

        //Look for Password
        if(!passwordConfig.passwordEncoder().matches(loginDto.getPassword(), userEntity.getPassword())) {
            throw new LoginException("Invalid Password", HttpStatus.BAD_REQUEST);
        }

//        //this is the same as above
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginDto.getUsername(),
//                        loginDto.getPassword()
//                )
//        );

        //Generate Token (currently the same as Register Token)
        String jwt = jwtService.generateToken(userEntity.getUsername());

        //All Status of Tokens related to UserEntity set to false
        revokeAllTokensByUser(userEntity);

        // Save new JWT to Token and UserEntity
        List<Token> savedTokens = saveTokens(jwt, userEntity);

        // Set Status of the new JWT at UserEntity to true
        userEntity.setStatus(true);
        UserEntity currentUser = userRepository.save(userEntity);

        // Set Status of new JWT at Token to true
        for (Token token : savedTokens) {
            token.setStatus(userEntity.isStatus());
            System.out.println("Token ID: " + token.getId());
            System.out.println("Token Status: " + token.isStatus());
        }

        //Save Tokens to Token and User Entity
        tokenRepository.saveAll(savedTokens);
        List<String> tokenStrings = savedTokens.stream()
                .map(Token::getToken)
                .collect(Collectors.toList());
        currentUser.setTokens(tokenStrings);

        return userMappers.maptoDto(userEntity);
    }

    @Override
    public UserDto register(RegisterDto registerDto) {

        //Check if theres any Existing Users
        Optional<UserEntity> checkUser = userRepository.findByUsername(registerDto.getUsername());
        if (checkUser.isPresent()) {
            throw new LoginException("User already exists", HttpStatus.BAD_REQUEST);
        }

        //Map RegisterDTO to UserEntity
        UserEntity userEntity = userMappers.maptoEntity(registerDto);
        userEntity.setStatus(false);
        UserEntity savedEntity = userRepository.save(userEntity);

        //Generate JWT using the UserEntity
        //Needs to be same as login
        String jwt = jwtService.generateToken(savedEntity.getUsername());

        //Save JWT to Token and UserEntity
        List<Token> savedTokens = saveTokens(jwt, savedEntity);

        // Set the token status based on the UserEntity Status
        for (Token token : savedTokens) {
            token.setStatus(savedEntity.isStatus());
        }

        //Save Tokens to Token and UserEntity
        tokenRepository.saveAll(savedTokens);
        List<String> tokenStrings = savedTokens.stream()
                .map(Token::getToken)
                .collect(Collectors.toList());
        savedEntity.setTokens(tokenStrings);

        return userMappers.maptoDto(savedEntity);
    }





    private List<Token> saveTokens(String jwt, UserEntity userEntity) {
        //Set new JWT to Token and Status to true
        Token token = new Token();
        token.setToken(jwt);
        token.setStatus(false);
        tokenRepository.save(token);

        //Save Token to UserEntity
        List<Token> savedTokens = new ArrayList<>();
        savedTokens.add(token);
        List<String> tokenStrings = savedTokens.stream()
                .map(Token::getToken)
                .collect(Collectors.toList());
        userEntity.setTokens(tokenStrings);

        return savedTokens;
    }

    private void revokeAllTokensByUser(UserEntity userEntity) {
        //Find all Tokens related to UserEntity
        List<Token> validTokens = tokenRepository.findAllTokensByPersonId(userEntity.getId());
        if(validTokens.isEmpty()) {
            return;
        }
        //Set Status of Tokens related to UserEntity to false
        validTokens.forEach(t-> {
            t.setStatus(false);
        });

        tokenRepository.saveAll(validTokens);
    }

}
