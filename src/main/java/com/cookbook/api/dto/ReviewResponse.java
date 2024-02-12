package com.cookbook.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReviewResponse {
    private List<ReviewDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

}
