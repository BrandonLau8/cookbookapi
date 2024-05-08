package com.cookbook.api.repository;

import com.cookbook.api.models.RoleEntity;
import com.cookbook.api.models.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByName(RoleType name);
}
