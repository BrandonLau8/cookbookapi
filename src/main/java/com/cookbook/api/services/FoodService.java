package com.cookbook.api.services;

import com.cookbook.api.dto.FoodDto;
import com.cookbook.api.models.Food;

public interface FoodService {
    FoodDto createFood(FoodDto foodDto);
}
