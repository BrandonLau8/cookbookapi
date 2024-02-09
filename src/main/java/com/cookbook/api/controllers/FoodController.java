package com.cookbook.api.controllers;

import com.cookbook.api.dto.FoodDto;
import com.cookbook.api.models.Food;
import com.cookbook.api.services.FoodService;
import org.apache.coyote.Response;
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
    public ResponseEntity<List<FoodDto>> getFoods() {
        return new ResponseEntity<>(foodService.getAllFood(), HttpStatus.OK);
    }

    @GetMapping("food/{id}")
    public ResponseEntity<FoodDto> getFoodById(@PathVariable int id) {
        return new ResponseEntity<>(foodService.getFoodById(id), HttpStatus.OK);
    }

    @PostMapping("food/create")
    @ResponseStatus(HttpStatus.CREATED) //note: not necessary to add httpstatus twice
    public ResponseEntity<FoodDto > createFood(@RequestBody FoodDto foodDto) {
        return new ResponseEntity<>(foodService.createFood(foodDto), HttpStatus.CREATED);
    }

    @PutMapping("food/{id}/update")
    public ResponseEntity<FoodDto> updateFood(@RequestBody FoodDto foodDto, @PathVariable("id") int foodId) {
        FoodDto foodResponse = foodService.updateFood(foodDto, foodId);
        return new ResponseEntity<>(foodResponse, HttpStatus.OK);
    }

    @DeleteMapping("food/{id}/delete")
    public ResponseEntity<String> deleteFood(@PathVariable("id") int foodId) {
        foodService.deleteFoodById(foodId);
        return new ResponseEntity<>("Food Deleted", HttpStatus.OK);
    }
}
