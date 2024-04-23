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

        UserEntity userEntity = userRepository.findByUsername(loginDto.getUsername()).orElseThrow();

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

        String jwt = jwtService.generateToken(userEntity.getUsername());
        revokeAllTokensByUser(userEntity);
        saveToken(jwt, userEntity);
        userEntity.setLoggedOut(false);


        return userMappers.maptoDto(userEntity);
    }

    @Override
    public UserDto register(RegisterDto registerDto) {
        Optional<UserEntity> checkUser = userRepository.findByUsername(registerDto.getUsername());

        if (checkUser.isPresent()) {
            throw new LoginException("User already exists", HttpStatus.BAD_REQUEST);
        }

        UserEntity userEntity = userMappers.maptoEntity(registerDto);

        UserEntity savedEntity = userRepository.save(userEntity);

        //Needs to be same as login
        String jwt = jwtService.generateToken(savedEntity.getUsername());

        List<Token> savedTokens = saveToken(jwt, savedEntity);
        savedEntity.setTokens(savedTokens);
        savedEntity.setLoggedOut(true);
        System.out.println(savedEntity.getTokens());

        return userMappers.maptoDto(savedEntity);
    }





    private List<Token> saveToken(String jwt, UserEntity userEntity) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);


        tokenRepository.save(token);

        List<Token> savedTokens = new ArrayList<>();
        savedTokens.add(token);

        userEntity.setTokens(savedTokens);

        return savedTokens;
    }

    private void revokeAllTokensByUser(UserEntity userEntity) {
        List<Token> validTokens = tokenRepository.findAllTokensByPersonId(userEntity.getId());
        if(validTokens.isEmpty()) {
            return;
        }
        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }

}
