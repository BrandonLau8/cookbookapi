package com.cookbook.api.impl;

import com.cookbook.api.dto.ReviewDto;
import com.cookbook.api.models.Review;
import com.cookbook.api.repository.ReviewRepository;
import com.cookbook.api.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository reviewRepository;
    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDto) {
        Review review = new Review();
        review.setReviewer(reviewDto.getReviewer());
        review.setContent(reviewDto.getContent());
        review.setRating(reviewDto.getRating());

        Review newReview = reviewRepository.save(review);

        ReviewDto reviewResponse = new ReviewDto();
        reviewResponse.setId(review.getId());
        reviewResponse.setReviewer(review.getReviewer());
        reviewResponse.setContent(review.getContent());
        reviewResponse.setRating(review.getRating());
        return reviewResponse;
    }
}
