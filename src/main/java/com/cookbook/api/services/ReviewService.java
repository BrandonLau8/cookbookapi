package com.cookbook.api.services;

import com.cookbook.api.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    ReviewDto createReview(ReviewDto reviewDto);
    List<ReviewDto> getAllReviews();
    ReviewDto getReviewById(int id);
    ReviewDto updateReview(ReviewDto reviewDto, int id);
    void deleteReview(int id);
}
