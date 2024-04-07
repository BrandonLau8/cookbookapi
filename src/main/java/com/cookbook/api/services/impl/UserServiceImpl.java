package com.cookbook.api.services.impl;

import com.cookbook.api.dto.LoginDto;
import com.cookbook.api.dto.UserDto;
import com.cookbook.api.exceptions.LoginException;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.UserRepository;
import com.cookbook.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto login(LoginDto loginDto) {
        UserEntity userEntity = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new LoginException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(loginDto.getPassword(), userEntity.getPassword())) {
            return maptoDto(userEntity);
        }
        throw new LoginException("Invalid Password", HttpStatus.BAD_REQUEST);
    }

    private UserDto maptoDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setUsername(userEntity.getUsername());
        userDto.setToken(userEntity.getPassword());
        return userDto;
    }
}
