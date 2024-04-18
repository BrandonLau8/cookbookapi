package com.cookbook.api.repository;

import com.cookbook.api.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    //Optional returns either an object with existing username or empty object
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
}
