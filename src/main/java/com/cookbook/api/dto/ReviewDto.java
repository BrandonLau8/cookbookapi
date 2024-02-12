package com.cookbook.api.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private int id;
    private String reviewer;
    private String comment;
    private int rating;
}
