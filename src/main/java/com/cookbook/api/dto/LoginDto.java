package com.cookbook.api.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginDto {
    private String username;
    private String password;

}