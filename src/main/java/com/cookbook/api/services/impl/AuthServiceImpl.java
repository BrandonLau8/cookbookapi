package com.cookbook.api.services.impl;

import com.cookbook.api.dto.LoginDto;
import com.cookbook.api.dto.RefreshTokenDto;
import com.cookbook.api.dto.RegisterDto;
import com.cookbook.api.dto.UserDto;
import com.cookbook.api.exceptions.LoginException;
import com.cookbook.api.mappers.UserMappers;
import com.cookbook.api.models.RefreshToken;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.RefreshTokenRepository;
import com.cookbook.api.repository.RoleRepository;
import com.cookbook.api.repository.UserRepository;
import com.cookbook.api.security.AuthEntryPoint;
import com.cookbook.api.security.DaoAuthProvider;
import com.cookbook.api.security.PasswordConfig;
import com.cookbook.api.security.UserDetailsServiceImpl;
import com.cookbook.api.services.AuthService;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.RefreshTokenService;
import com.cookbook.api.services.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Data
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordConfig passwordConfig;

    private final UserService userService;

    private final JwtService jwtService;

    private final UserMappers userMappers;

    private final UserDetailsServiceImpl userDetailsService;

    private final DaoAuthProvider daoAuthProvider;

    private final RefreshTokenService refreshTokenService;

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordConfig passwordConfig, UserService userService, JwtService jwtService, UserMappers userMappers, UserDetailsServiceImpl userDetailsService, DaoAuthProvider daoAuthProvider, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordConfig = passwordConfig;
        this.userService = userService;
        this.jwtService = jwtService;
        this.userMappers = userMappers;
        this.userDetailsService = userDetailsService;
        this.daoAuthProvider = daoAuthProvider;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public UserDto refreshLogin(RefreshTokenDto refreshTokenDto) {
        //Find Refresh token
        RefreshToken userToken = refreshTokenRepository.findByToken(refreshTokenDto.getToken()).orElseThrow(() -> new RuntimeException("No refresh token"));


        //If token found, generate a new Access token
        String jwt = jwtService.generateToken(userToken.getPerson().getUsername());

        //Update refreshToken
        userToken.setExpiryDate(Instant.now().plusMillis(60000));

        //Set new access token to userdto
        UserEntity currentUser = userRepository.findByUsername(userToken.getPerson().getUsername()).orElseThrow(() -> new RuntimeException("Cannot find username"));
        UserDto userDto = userMappers.maptoDto(currentUser);
        userDto.setAccessToken(jwt);
        userDto.setToken(userToken.getToken());

        return userDto;
    }

    @Override
    public UserDto login(LoginDto loginDto) {
        //Auth with dao
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authenticated = daoAuthProvider.authenticate(authentication);

        if (authenticated.isAuthenticated()) {
            // If authentication is successful, generate a access token and refresh token using username from principal
            if (authenticated.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authenticated.getPrincipal();

                String jwt = jwtService.generateToken(userDetails.getUsername());

                //Put token into userdto
                UserDto userDto = userMappers.detailToDto(userDetails);
                userDto.setAccessToken(jwt);


                //check if there are any existing refresh tokens
                refreshTokenRepository.findByPersonUsername(userDetails.getUsername()).ifPresentOrElse((existingRefreshToken) -> {
                    existingRefreshToken.setExpiryDate(Instant.now().plusMillis(600000));
                    refreshTokenRepository.save(existingRefreshToken);
                    userDto.setToken(existingRefreshToken.getToken());
                }, () -> {
                    RefreshToken newRefreshToken = refreshTokenService.createRefeshToken(userDto.getUsername());
                    userDto.setToken(newRefreshToken.getToken());
                });

                return userDto;
            } else {
                throw new BadCredentialsException("User principal is not of type UserDetails");
            }
        } else {
//            throw new BadCredentialsException("Authentication Failed");
            throw new LoginException("Login error", HttpStatus.UNAUTHORIZED);

        }
    }

    @Override
    public UserDto register(RegisterDto registerDto) {
        userRepository.findByUsername(registerDto.getUsername()).ifPresent((userEntity) -> {
            throw new LoginException("User already exists", HttpStatus.BAD_REQUEST);
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
