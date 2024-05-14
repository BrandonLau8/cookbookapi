package com.cookbook.api.repository;

import com.cookbook.api.models.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, Integer> {
    Optional<InvalidToken> findByToken(String token);
}
