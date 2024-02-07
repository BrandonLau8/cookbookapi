package com.cookbook.api.controllers;

import com.cookbook.api.models.Ingredient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class IngredientControllers {
    @GetMapping("ingredients")
    public ResponseEntity<List<Ingredient>> getIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1, "egg", 5, "scramble them"));
        return new ResponseEntity<>(ingredients, HttpStatus.OK);

    }
}

