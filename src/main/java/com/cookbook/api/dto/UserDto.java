package com.cookbook.api.dto;


import com.cookbook.api.models.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private int id;
    private String firstname;
    private String lastname;
    private String username;
    private String token;
    private boolean status;

}
