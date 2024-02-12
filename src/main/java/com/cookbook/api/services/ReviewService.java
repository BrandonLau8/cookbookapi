package com.cookbook.api.services;

import com.cookbook.api.dto.ReviewDto;
import com.cookbook.api.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewDto createReview(int foodId, ReviewDto reviewDto);
    ReviewResponse getReviewsByFoodId(int foodId, int pageNo, int pageSize);
    ReviewDto getReviewById(int foodId, int reviewId);
    ReviewDto updateReview(int foodId, ReviewDto reviewDto, int reviewId);
    void deleteReview(int foodId, int reviewId);
}
