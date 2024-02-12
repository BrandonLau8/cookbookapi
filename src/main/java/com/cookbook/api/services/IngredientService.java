package com.cookbook.api.services;

import com.cookbook.api.dto.IngredientDto;
import com.cookbook.api.dto.IngredientResponse;

import java.util.List;

public interface IngredientService {
    IngredientDto createIngredient(int foodId, IngredientDto ingredientDto);

    IngredientResponse getIngredientsByFoodId(int foodId, int pageNo, int pageSize);
    IngredientDto getIngredientById(int foodId, int ingredientId);

    IngredientDto updateIngredient(int foodId, IngredientDto ingredientDto, int ingredientId);
    void deleteIngredient(int foodId, int ingredientId);
}
