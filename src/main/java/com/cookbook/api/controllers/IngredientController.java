package com.cookbook.api.controllers;

import com.cookbook.api.dto.IngredientDto;
import com.cookbook.api.impl.IngredientServiceImpl;
import com.cookbook.api.models.Ingredient;
import com.cookbook.api.services.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class IngredientController {

    private IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("ingredients")
    public ResponseEntity<List<IngredientDto>> getIngredients() {
        return new ResponseEntity<>(ingredientService.getAllIngredients(), HttpStatus.OK);
    }

    @PostMapping("ingredient/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<IngredientDto> createIngredient(@RequestBody IngredientDto ingredientDto) {
        return new ResponseEntity<>(ingredientService.createIngredient(ingredientDto), HttpStatus.CREATED);
    }
}

