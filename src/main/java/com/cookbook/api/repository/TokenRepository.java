package com.cookbook.api.repository;

import com.cookbook.api.models.Token;
import com.cookbook.api.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    List<Token> findAllTokensByPersonId(Integer personId);

    Optional<Token> findByToken(String token);
}
