package com.cookbook.api.controllers;

import com.cookbook.api.models.Food;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController //indicates to Spring this class will handle HTTP req/res
@RequestMapping("/api/") //add "api" to route
public class FoodController {

    @GetMapping("food")
    public ResponseEntity<List<Food>> getFoods() {
        List<Food> foods = new ArrayList<>();
        foods.add(new Food(1, "pasta"));
        foods.add(new Food(2, "potato"));
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }
}
