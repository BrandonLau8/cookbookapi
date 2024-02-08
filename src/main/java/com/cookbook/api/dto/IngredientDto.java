package com.cookbook.api.dto;

import lombok.Data;

@Data
public class IngredientDto {
    private int id;
    private String name;
    private int quantity;
    private String description;
}
