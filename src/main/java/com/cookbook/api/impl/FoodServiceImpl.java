package com.cookbook.api.impl;

import com.cookbook.api.dto.FoodDto;
import com.cookbook.api.models.Food;
import com.cookbook.api.repository.FoodRepository;
import com.cookbook.api.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodServiceImpl implements FoodService {
    private FoodRepository foodRepository;
    @Autowired //more common above constructor than instance. saves from having to manually create and wire objects together
    public FoodServiceImpl(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public FoodDto createFood(FoodDto foodDto) {
        Food food = new Food();
        food.setName(foodDto.getName());

        Food newFood = foodRepository.save(food);

        FoodDto foodResponse = new FoodDto();
        foodResponse.setId(newFood.getId());
        foodResponse.setName(newFood.getName());
        return foodResponse;
    }
}
