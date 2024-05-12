package com.cookbook.api.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RefreshTokenDto {
    private String token;
}
