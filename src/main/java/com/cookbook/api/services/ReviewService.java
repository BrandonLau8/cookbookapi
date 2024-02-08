package com.cookbook.api.services;

import com.cookbook.api.dto.ReviewDto;

public interface ReviewService {
    ReviewDto createReview(ReviewDto reviewDto);
}
