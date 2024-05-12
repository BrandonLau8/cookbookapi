package com.cookbook.api.dto;

import com.cookbook.api.models.RoleEntity;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String accessToken;
    private String token;
    private Set<RoleEntity> roles;
}
