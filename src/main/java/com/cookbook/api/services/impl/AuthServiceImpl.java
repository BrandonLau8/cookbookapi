package com.cookbook.api.services.impl;

import com.cookbook.api.dto.AuthResponseDto;
import com.cookbook.api.dto.LoginDto;
import com.cookbook.api.dto.RegisterDto;
import com.cookbook.api.dto.UserDto;
import com.cookbook.api.exceptions.LoginException;
import com.cookbook.api.models.RoleEntity;
import com.cookbook.api.models.RoleType;
import com.cookbook.api.models.Token;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.RoleRepository;
import com.cookbook.api.repository.UserRepository;
import com.cookbook.api.security.PasswordConfig;
import com.cookbook.api.services.AuthService;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordConfig passwordConfig;

    private final UserService userService;

    private final JwtService jwtService;


    @Override
    public UserDto login(LoginDto loginDto) {
        UserEntity userEntity = userService.loadUserByUsername(loginDto);

        if (passwordConfig.passwordEncoder().matches(loginDto.getPassword(), userEntity.getPassword())) {
            return maptoDto(userEntity);
        }
        throw new LoginException("Invalid Password", HttpStatus.BAD_REQUEST);
    }

    @Override
    public AuthResponseDto register(RegisterDto registerDto) {
        Optional<UserEntity> checkUser = userRepository.findByUsername(registerDto.getUsername());

        if(checkUser.isPresent()) {
            throw new LoginException("User already exists", HttpStatus.BAD_REQUEST);
        }

        UserEntity userEntity = maptoEntity(registerDto);


        UserEntity savedEntity = userRepository.save(userEntity);
        String jwt = jwtService.generateToken(savedEntity);
        saveRegToken(jwt,savedEntity);

        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setRegToken(jwt);

        return authResponseDto;
    }

    private UserDto maptoDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setUsername(userEntity.getUsername());
        userDto.setToken(userEntity.getPassword());
        return userDto;
    }

    private UserEntity maptoEntity(RegisterDto registerDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerDto.getUsername());
        userEntity.setPassword(passwordConfig.passwordEncoder().encode(registerDto.getPassword()));

        RoleEntity assignUserRole = new RoleEntity();
        assignUserRole.setName(RoleType.USER);
        roleRepository.save(assignUserRole);

        return userEntity;
    }

    private void saveRegToken(String jwt, UserEntity userEntity) {
        Token token = new Token();
        token.setRegToken(jwt);
        token.setLoggedOut(false);
        token.setUsername(userEntity.getUsername());
    }
}
