package com.cookbook.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class IngredientResponse {
    private List<IngredientDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
