package com.cookbook.api.impl;

import com.cookbook.api.dto.FoodDto;
import com.cookbook.api.exceptions.FoodNotFoundException;
import com.cookbook.api.models.Food;
import com.cookbook.api.repository.FoodRepository;
import com.cookbook.api.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<FoodDto> getAllFood() {
        List<Food> food = foodRepository.findAll();
        //Food food333 = foodRepository.findById(333).orElseThrow(()->new FoodNotFoundException("Food not found by id"));
        //mapping converts values to FoodDto object.
        return food.stream().map(x->mapToDto(x)).collect(Collectors.toList());
    }

    @Override
    public FoodDto getFoodById(int id) {
        Food food = foodRepository.findById(id).orElseThrow(()->new FoodNotFoundException("Food not found by Id"));
        return mapToDto(food);
    }

    @Override
    public FoodDto updateFood(FoodDto foodDto, int id) {
        Food food = foodRepository.findById(id).orElseThrow(()->
                new FoodNotFoundException("Food not found by Id"));
        food.setName(foodDto.getName());

        Food updatedFood = foodRepository.save(food);
        return mapToDto(updatedFood);
    }

    @Override
    public void deleteFood(int id) {
        Food food = foodRepository.findById(id).orElseThrow(()->
                new FoodNotFoundException("Food not found by Id"));
        foodRepository.delete(food);
    }

    private FoodDto mapToDto(Food food) {
        FoodDto foodDto = new FoodDto();
        foodDto.setName(food.getName());
        foodDto.setId(food.getId());
        return foodDto;
    }

    private Food mapToEntity(FoodDto foodDto) {
        Food food = new Food();
        food.setName(foodDto.getName());
        return food;
    }
}

