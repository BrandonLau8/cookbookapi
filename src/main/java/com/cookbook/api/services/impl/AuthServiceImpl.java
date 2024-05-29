package com.cookbook.api.services.impl;

import com.cookbook.api.dto.LoginDto;
import com.cookbook.api.dto.RefreshTokenDto;
import com.cookbook.api.dto.RegisterDto;
import com.cookbook.api.dto.UserDto;
import com.cookbook.api.exceptions.LoginException;
import com.cookbook.api.exceptions.RegistrationException;
import com.cookbook.api.mappers.UserMappers;
import com.cookbook.api.models.RefreshToken;
import com.cookbook.api.models.RoleEntity;
import com.cookbook.api.models.RoleType;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.RefreshTokenRepository;
import com.cookbook.api.repository.RoleRepository;
import com.cookbook.api.repository.UserRepository;
import com.cookbook.api.security.DaoAuthProvider;
import com.cookbook.api.security.PasswordConfig;
import com.cookbook.api.security.UserDetailsServiceImpl;
import com.cookbook.api.services.AuthService;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordConfig passwordConfig, UserService userService, JwtService jwtService, UserMappers userMappers, UserDetailsServiceImpl userDetailsService, DaoAuthProvider daoAuthProvider, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordConfig = passwordConfig;
        this.userService = userService;
        this.jwtService = jwtService;
        this.userMappers = userMappers;
        this.userDetailsService = userDetailsService;
        this.daoAuthProvider = daoAuthProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public UserDto login(LoginDto loginDto) {
        //Authentication with Dao
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authenticated = daoAuthProvider.authenticate(authentication);

        if (authenticated.isAuthenticated()) {
            // If authentication is successful,
            if (authenticated.getPrincipal() instanceof UserDetails) {
                //generate a access token and refresh token using username from principal
                UserDetails userDetails = (UserDetails) authenticated.getPrincipal();
                Set<RoleEntity> roles = authenticated.getAuthorities().stream()
                                .map(authority -> {
                                        RoleEntity roleEntity = new RoleEntity();
                                        roleEntity.setName(RoleType.valueOf(authority.getAuthority()));
                                        return roleEntity;
                                })
                        .collect(Collectors.toSet());
                return userMappers.detailToDto(userDetails, roles);
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

        if(userRepository.findByUsername(registerDto.getUsername()).isEmpty()) {
            UserEntity userEntity = userMappers.maptoEntity(registerDto);

            Set<RoleEntity> roles = new HashSet<>();
            List<RoleEntity> userRole = roleRepository.findByName(RoleType.USER);

            userEntity.setRoles(roles);

            userRepository.save(userEntity);

            return userMappers.maptoDto(userEntity);
        } else {
            throw new RegistrationException("User already exists", HttpStatus.BAD_REQUEST);
        }
    }

}
