package com.cookbook.api.dto;

import com.cookbook.api.models.Food;
import lombok.Data;

import java.util.List;

@Data
public class FoodResponse {
    private List<FoodDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
