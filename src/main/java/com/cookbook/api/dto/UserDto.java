package com.cookbook.api.dto;

import com.cookbook.api.models.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
public class UserDto {
    private int id;
    private String username;
    private String token;
    private Set<RoleEntity> roles;

    public UserDto() {
    }

    public UserDto(int id, String username, String token, Set<RoleEntity> roles) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.roles = roles;
    }
}
