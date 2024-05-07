package com.cookbook.api.services.impl;

import com.cookbook.api.dto.LoginDto;
import com.cookbook.api.dto.RegisterDto;
import com.cookbook.api.dto.UserDto;
import com.cookbook.api.exceptions.LoginException;
import com.cookbook.api.mappers.UserMappers;
import com.cookbook.api.models.RoleEntity;
import com.cookbook.api.models.RoleType;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.RoleRepository;
import com.cookbook.api.repository.UserRepository;
import com.cookbook.api.security.PasswordConfig;
import com.cookbook.api.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.Optional;


@Service
@DependsOn("userDetailsService") // Specify the name of the UserDetailsService bean
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final UserMappers userMappers;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMappers userMappers) {
        this.userRepository = userRepository;
        this.userMappers = userMappers;
    }

    @Override
    public UserDto findByUsername(String username) {
            UserEntity userEntity = userRepository.findByUsername(username)
                    .orElseThrow(() -> new LoginException("Username not found", HttpStatus.NOT_FOUND));

            return userMappers.maptoDto(userEntity);
    }
}
