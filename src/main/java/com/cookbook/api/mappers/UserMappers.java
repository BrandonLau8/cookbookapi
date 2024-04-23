package com.cookbook.api.mappers;

import com.cookbook.api.dto.RegisterDto;
import com.cookbook.api.dto.UserDto;
import com.cookbook.api.models.RoleEntity;
import com.cookbook.api.models.RoleType;
import com.cookbook.api.models.Token;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.RoleRepository;
import com.cookbook.api.security.PasswordConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserMappers {

    private final PasswordConfig passwordConfig;

    private final RoleRepository roleRepository;

    public UserDto maptoDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setFirstname(userEntity.getFirstname());
        userDto.setLastname(userEntity.getLastname());
        userDto.setUsername(userEntity.getUsername());
        userDto.setLoggedOut(userEntity.isLoggedOut());


        Optional<Token> firstToken = userEntity.getTokens().stream().findFirst();
        firstToken.ifPresent(token -> userDto.setToken(token.getToken()));


        return userDto;
    }

    public UserEntity maptoEntity(RegisterDto registerDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstname(registerDto.getFirstname());
        userEntity.setLastname(registerDto.getLastname());
        userEntity.setUsername(registerDto.getUsername());
        userEntity.setPassword(passwordConfig.passwordEncoder().encode(registerDto.getPassword()));


        RoleEntity assignUserRole = new RoleEntity();
        assignUserRole.setName(RoleType.USER);
        roleRepository.save(assignUserRole);

        return userEntity;
    }
}
