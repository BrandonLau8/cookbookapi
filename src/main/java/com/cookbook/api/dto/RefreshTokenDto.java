package com.cookbook.api.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefreshTokenDto {
    private String token;
}
