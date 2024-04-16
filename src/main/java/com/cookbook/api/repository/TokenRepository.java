package com.cookbook.api.repository;

import com.cookbook.api.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllTokensByUser(Long userId);

//    Optional<Token> findByRegToken(String token);
}
