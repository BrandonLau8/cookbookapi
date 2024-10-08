package com.cookbook.api.mappers;

import com.cookbook.api.dto.RegisterDto;
import com.cookbook.api.dto.UserDto;
import com.cookbook.api.models.RoleEntity;
import com.cookbook.api.models.RoleType;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.RoleRepository;
import com.cookbook.api.security.PasswordConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMappers {
    private final PasswordConfig passwordConfig;

    private final RoleRepository roleRepository;

    @Autowired
    public UserMappers(PasswordConfig passwordConfig, RoleRepository roleRepository) {
        this.passwordConfig = passwordConfig;
        this.roleRepository = roleRepository;
    }

    public UserDto maptoDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setUsername(userEntity.getUsername());
        userDto.setRoles(userEntity.getRoles());
        return userDto;
    }

    public UserDto detailToDto(UserDetails userDetails, Set<RoleEntity> roles) {
        UserDto userDto = new UserDto();
        userDto.setUsername(userDetails.getUsername());
        userDto.setRoles(roles);
        return userDto;
    }

    public UserEntity maptoEntity(RegisterDto registerDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerDto.getUsername());
        userEntity.setPassword(passwordConfig.passwordEncoder().encode(registerDto.getPassword()));


        return userEntity;
    }
}
