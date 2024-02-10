package com.cookbook.api.services;

import com.cookbook.api.dto.FoodDto;
import com.cookbook.api.models.Food;

import java.util.List;

public interface FoodService {
    FoodDto createFood(FoodDto foodDto);
    List<FoodDto> getAllFood();
    FoodDto getFoodById(int id);
    FoodDto updateFood(FoodDto foodDto, int id);
    void deleteFood(int id);
}
