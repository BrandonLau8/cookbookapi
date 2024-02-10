package com.cookbook.api.services;

import com.cookbook.api.dto.IngredientDto;

import java.util.List;

public interface IngredientService {
    IngredientDto createIngredient(IngredientDto ingredientDto);

    List<IngredientDto> getAllIngredients();
    IngredientDto getIngredientById(int id);

    IngredientDto updateIngredient(IngredientDto ingredientDto, int id);
    void deleteIngredient(int id);
}
