package com.cookbook.api.repository;

import com.cookbook.api.models.Ingredient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    Slice<Ingredient> findByFoodId(int foodId, Pageable pageable);
}
