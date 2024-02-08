package com.cookbook.api.controllers;

import com.cookbook.api.models.Ingredient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class IngredientController {
    @GetMapping("ingredients")
    public ResponseEntity<List<Ingredient>> getIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1, "egg", 5, "scramble them"));
        return new ResponseEntity<>(ingredients, HttpStatus.OK);
    }

    @PostMapping("ingredient/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Ingredient> createIngredient(@RequestBody Ingredient ingredient) {
        return new ResponseEntity<>(ingredient, HttpStatus.CREATED);
    }
}

