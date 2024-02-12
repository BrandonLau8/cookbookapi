package com.cookbook.api.services;

import com.cookbook.api.dto.FoodDto;
import com.cookbook.api.dto.FoodResponse;
import com.cookbook.api.models.Food;

import java.util.List;

public interface FoodService {
    FoodDto createFood(FoodDto foodDto);
    FoodResponse getAllFood(int pageNo, int pageSize);
    FoodDto getFoodById(int id);
    FoodDto updateFood(FoodDto foodDto, int id);
    void deleteFood(int id);
}
