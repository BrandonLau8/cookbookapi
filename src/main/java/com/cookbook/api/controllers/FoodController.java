package com.cookbook.api.controllers;

import com.cookbook.api.dto.FoodDto;
import com.cookbook.api.models.Food;
import com.cookbook.api.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController //indicates to Spring this class will handle HTTP req/res
@RequestMapping("/api/") //add "api" to route
public class FoodController {

    private FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("foods")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Food>> getFoods() {
        List<Food> foods = new ArrayList<>();
        foods.add(new Food(1, "pasta"));
        foods.add(new Food(2, "potato"));
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @PostMapping("food/create")
    @ResponseStatus(HttpStatus.CREATED) //note: not necessary to add httpstatus twice
    public ResponseEntity<FoodDto> createFood(@RequestBody FoodDto foodDto) {
        return new ResponseEntity<>(foodService.createFood(foodDto), HttpStatus.CREATED);
    }
}
