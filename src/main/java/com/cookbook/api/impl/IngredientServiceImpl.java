package com.cookbook.api.impl;

import com.cookbook.api.dto.IngredientDto;
import com.cookbook.api.exceptions.IngredientNotFoundException;
import com.cookbook.api.models.Ingredient;
import com.cookbook.api.repository.IngredientRepository;
import com.cookbook.api.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        ingredientResponse.setId(newIngredient.getId());
        ingredientResponse.setName(newIngredient.getName());
        ingredientResponse.setDescription(newIngredient.getDescription());
        ingredientResponse.setQuantity(newIngredient.getQuantity());
        return ingredientResponse;
    }

    @Override
    public List<IngredientDto> getAllIngredients() {
        List<Ingredient> ingredient = ingredientRepository.findAll();
        return ingredient.stream().map((x)->mapToDto(x)).collect(Collectors.toList());
    }

    @Override
    public IngredientDto getIngredientById(int id) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(
                () -> new IngredientNotFoundException("Ingredient not found by Id."));
        return mapToDto(ingredient);
    }

    @Override
    public IngredientDto updateIngredient(IngredientDto ingredientDto, int id) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(
                ()-> new IngredientNotFoundException("Ingredient not found by Id."));
        ingredient.setName(ingredientDto.getName());
        ingredient.setDescription(ingredientDto.getDescription());
        ingredient.setQuantity(ingredientDto.getQuantity());

        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        return mapToDto(updatedIngredient);
    }

    @Override
    public void deleteIngredient(int id) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(
                ()->new IngredientNotFoundException("Ingredient not found by id"));
        ingredientRepository.delete(ingredient);
    }

    private IngredientDto mapToDto(Ingredient ingredient) {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(ingredient.getId());
        ingredientDto.setName(ingredient.getName());
        ingredientDto.setQuantity(ingredient.getQuantity());
        ingredientDto.setDescription(ingredient.getDescription());
        return ingredientDto;
    }

    private Ingredient mapToEntity(IngredientDto ingredientDto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDto.getName());
        ingredient.setQuantity(ingredientDto.getQuantity());
        ingredient.setDescription(ingredientDto.getDescription());
        return ingredient;
    }


}
