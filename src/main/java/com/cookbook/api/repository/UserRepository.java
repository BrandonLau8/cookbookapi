package com.cookbook.api.repository;

import com.cookbook.api.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    //Optional returns either an object with existing username or empty object
    Optional<UserEntity> findByUsername(String username);

}
