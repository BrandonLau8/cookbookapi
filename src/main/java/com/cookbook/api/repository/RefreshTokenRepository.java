package com.cookbook.api.repository;

import com.cookbook.api.dto.UserDto;
import com.cookbook.api.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String refreshToken);

    Optional <RefreshToken> findByPersonUsername(String username);
}
