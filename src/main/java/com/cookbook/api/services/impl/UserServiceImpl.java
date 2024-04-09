package com.cookbook.api.services.impl;

import com.cookbook.api.dto.LoginDto;
import com.cookbook.api.dto.RegisterDto;
import com.cookbook.api.dto.UserDto;
import com.cookbook.api.exceptions.LoginException;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.UserRepository;
import com.cookbook.api.security.PasswordConfig;
import com.cookbook.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordConfig passwordConfig;

    @Override
    public UserDto login(LoginDto loginDto) {
        UserEntity userEntity = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new LoginException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordConfig.passwordEncoder().matches(loginDto.getPassword(), userEntity.getPassword())) {
            return maptoDto(userEntity);
        }
        throw new LoginException("Invalid Password", HttpStatus.BAD_REQUEST);
    }

    @Override
    public UserDto register(RegisterDto registerDto) {
        Optional<UserEntity> checkUser = userRepository.findByUsername(registerDto.getUsername());

        if(checkUser.isPresent()) {
            throw new LoginException("User already exists", HttpStatus.BAD_REQUEST);
        }

        UserEntity userEntity = maptoEntity(registerDto);
        userEntity.setPassword(passwordConfig.passwordEncoder().encode(registerDto.getPassword()));

        UserEntity savedEntity = userRepository.save(userEntity);

        return maptoDto(savedEntity);
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
        return userEntity;
    }

    @Override
    public UserDto findByLogin(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(()-> new LoginException("Username not found",HttpStatus.NOT_FOUND));
        return maptoDto(userEntity);
    }
}
