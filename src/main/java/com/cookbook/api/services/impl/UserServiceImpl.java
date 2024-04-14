package com.cookbook.api.services.impl;

import com.cookbook.api.dto.AuthResponseDto;
import com.cookbook.api.dto.LoginDto;
import com.cookbook.api.dto.RegisterDto;
import com.cookbook.api.dto.UserDto;
import com.cookbook.api.exceptions.LoginException;
import com.cookbook.api.models.RoleEntity;
import com.cookbook.api.models.RoleType;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.RoleRepository;
import com.cookbook.api.repository.UserRepository;
import com.cookbook.api.security.PasswordConfig;
import com.cookbook.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public abstract class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public UserEntity loadUserByUsername(LoginDto loginDto) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(()-> new LoginException("Username not found", HttpStatus.NOT_FOUND));

        return userEntity;
    }

}
