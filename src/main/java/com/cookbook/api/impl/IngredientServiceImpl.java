package com.cookbook.api.impl;

import com.cookbook.api.dto.IngredientDto;
import com.cookbook.api.models.Ingredient;
import com.cookbook.api.repository.IngredientRepository;
import com.cookbook.api.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientServiceImpl implements IngredientService {
    private IngredientRepository ingredientRepository;

    @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public IngredientDto createIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDto.getName());
        ingredient.setDescription(ingredientDto.getDescription());
        ingredient.setQuantity(ingredientDto.getQuantity());

        Ingredient newIngredient = ingredientRepository.save(ingredient);

        IngredientDto ingredientResponse = new IngredientDto();
        ingredientResponse.setId(ingredient.getId());
        ingredientResponse.setName(ingredient.getName());
        ingredientResponse.setDescription(ingredient.getDescription());
        ingredientResponse.setQuantity(ingredient.getQuantity());
        return ingredientResponse;
    }
}
