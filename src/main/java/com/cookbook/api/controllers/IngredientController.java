package com.cookbook.api.controllers;

import com.cookbook.api.dto.IngredientDto;
import com.cookbook.api.dto.IngredientResponse;
import com.cookbook.api.services.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class IngredientController {

    private IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("{foodId}/ingredients")
    public ResponseEntity<IngredientResponse> getIngredientsByFoodId(
            @PathVariable(value = "foodId") int foodId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(ingredientService.getIngredientsByFoodId(foodId, pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("{foodId}/ingredient/{id}")
    public ResponseEntity<IngredientDto> getIngredientById(
            @PathVariable(value = "foodId") int foodId,
            @PathVariable(value = "id") int ingredientId) {
        return new ResponseEntity<>(ingredientService.getIngredientById(foodId, ingredientId), HttpStatus.OK);
    }

    @PostMapping("{foodId}/ingredient")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<IngredientDto> createIngredient(
            @PathVariable(value = "foodId") int foodId,
            @RequestBody IngredientDto ingredientDto) {
        return new ResponseEntity<>(ingredientService.createIngredient(foodId, ingredientDto), HttpStatus.CREATED);
    }

    @PutMapping("{foodId}/ingredient/{id}")
    public ResponseEntity<IngredientDto> updateIngredient(
            @PathVariable(value = "foodId") int foodId,
            @RequestBody IngredientDto ingredientDto,
            @PathVariable(value = "id") int ingredientId) {
        IngredientDto ingredientResponse = ingredientService.updateIngredient(
                foodId, ingredientDto, ingredientId);
        return new ResponseEntity<>(ingredientResponse, HttpStatus.OK);
    }

    @DeleteMapping("{foodId}/ingredient/{id}")
    public ResponseEntity<String> deleteIngredient(
            @PathVariable(value = "foodId") int foodId,
            @PathVariable(value = "ingredientId") int ingredientId){
        ingredientService.deleteIngredient(foodId, ingredientId);
        return new ResponseEntity<>("Ingredient deleted", HttpStatus.OK);
    }
}

