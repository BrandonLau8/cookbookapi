package com.cookbook.api.services.impl;

import com.cookbook.api.dto.IngredientDto;
import com.cookbook.api.dto.IngredientResponse;
import com.cookbook.api.exceptions.FoodNotFoundException;
import com.cookbook.api.exceptions.IngredientNotFoundException;
import com.cookbook.api.models.Food;
import com.cookbook.api.models.Ingredient;
import com.cookbook.api.repository.FoodRepository;
import com.cookbook.api.repository.IngredientRepository;
import com.cookbook.api.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {
    private FoodRepository foodRepository;
    private IngredientRepository ingredientRepository;


    @Autowired
    public IngredientServiceImpl(
            FoodRepository foodRepository,
            IngredientRepository ingredientRepository) {
        this.foodRepository = foodRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public IngredientDto createIngredient(int foodId, IngredientDto ingredientDto) {
        Ingredient ingredient = mapToEntity(ingredientDto);
        Food food = foodRepository.findById(foodId).orElseThrow(()->
                new FoodNotFoundException("Food not found by Id."));

        ingredient.setFood(food);

        Ingredient newIngredient = ingredientRepository.save(ingredient);

        return mapToDto(newIngredient);
    }

    @Override
    public IngredientResponse getIngredientsByFoodId(
            int foodId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Slice<Ingredient> ingredients = ingredientRepository.findByFoodId(foodId, pageable);
        List<Ingredient> listOfIngredients = ingredients.getContent();
        List<IngredientDto> content = listOfIngredients.stream().map((x)->mapToDto(x)).collect(Collectors.toList());

        IngredientResponse ingredientResponse = new IngredientResponse();
        ingredientResponse.setContent(content);
        ingredientResponse.setPageNo(ingredients.getNumber());
        ingredientResponse.setPageSize(ingredients.getSize());
        ingredientResponse.setTotalElements(ingredients.getNumberOfElements());
        ingredientResponse.setLast(ingredients.isLast());


        return ingredientResponse;
    }

    @Override
    public IngredientDto getIngredientById(int foodId, int ingredientId) {
        Food food = foodRepository.findById(foodId).orElseThrow(()->
                new FoodNotFoundException("Food not found by Id."));
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() ->
                new IngredientNotFoundException("Ingredient not found by Id."));

        if(ingredient.getFood().getId() != food.getId()) {
            throw new FoodNotFoundException("This ingredient does not belong to a Food.");
        }
        return mapToDto(ingredient);
    }

    @Override
    public IngredientDto updateIngredient(int foodId, IngredientDto ingredientDto, int ingredientId) {
        Food food = foodRepository.findById(foodId).orElseThrow(()->
                new FoodNotFoundException("Food not found by id."));
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(()->
                new IngredientNotFoundException("Ingredient not found by id."));

        if(ingredient.getFood().getId() != food.getId()){
            throw new FoodNotFoundException("Ingredient does not belong with food.");
        }

        ingredient.setName(ingredientDto.getName());
        ingredient.setDescription(ingredientDto.getDescription());
        ingredient.setQuantity(ingredientDto.getQuantity());

        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        return mapToDto(updatedIngredient);
    }

    @Override
    public void deleteIngredient(int foodId, int ingredientId) {
        Food food = foodRepository.findById(foodId).orElseThrow(()->
                new FoodNotFoundException("Food not found by id."));
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(()->
                new IngredientNotFoundException("Ingredient not found by id"));

        if(ingredient.getFood().getId() != food.getId()) {
            throw new FoodNotFoundException("Ingredient does not belong with food.");
        }

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
