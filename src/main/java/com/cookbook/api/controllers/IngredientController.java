package com.cookbook.api.controllers;

import com.cookbook.api.dto.IngredientDto;
import com.cookbook.api.impl.IngredientServiceImpl;
import com.cookbook.api.models.Ingredient;
import com.cookbook.api.services.IngredientService;
import org.apache.coyote.Response;
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

    @GetMapping("ingredient/{id}")
    public ResponseEntity<IngredientDto> getIngredientById(@PathVariable("id") int ingredientId) {
        return new ResponseEntity<>(ingredientService.getIngredientById(ingredientId), HttpStatus.OK);
    }

    @PostMapping("ingredient/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<IngredientDto> createIngredient(@RequestBody IngredientDto ingredientDto) {
        return new ResponseEntity<>(ingredientService.createIngredient(ingredientDto), HttpStatus.CREATED);
    }

    @PutMapping("ingredient/{id}/update")
    public ResponseEntity<IngredientDto> updateIngredient(
            @RequestBody IngredientDto ingredientDto, @PathVariable("id") int ingredientId) {
        IngredientDto ingredientResponse = ingredientService.updateIngredient(ingredientDto, ingredientId);
        return new ResponseEntity<>(ingredientResponse, HttpStatus.OK);
    }

    @DeleteMapping("ingredient/{id}/delete")
    public ResponseEntity<String> deleteIngredient(@PathVariable("id") int ingredientId){
        ingredientService.deleteIngredient(ingredientId);
        return new ResponseEntity<>("Ingredient deleted", HttpStatus.OK);
    }
}

