package com.cookbook.api.services;

import com.cookbook.api.dto.IngredientDto;

public interface IngredientService {
    IngredientDto createIngredient(IngredientDto ingredientDto);
}
