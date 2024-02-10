package com.cookbook.api.impl;

import com.cookbook.api.dto.ReviewDto;
import com.cookbook.api.exceptions.ReviewNotFoundException;
import com.cookbook.api.models.Review;
import com.cookbook.api.repository.ReviewRepository;
import com.cookbook.api.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<ReviewDto> getAllReviews() {
        List<Review> review = reviewRepository.findAll();
        return review.stream().map((x)->mapToDto(x)).collect(Collectors.toList());

    }

    @Override
    public ReviewDto getReviewById(int id) {
        Review review = reviewRepository.findById(id).orElseThrow(()->
                new ReviewNotFoundException("Review not found by Id"));
        return mapToDto(review);
    }

    @Override
    public ReviewDto updateReview(ReviewDto reviewDto, int id) {
        Review review = reviewRepository.findById(id).orElseThrow(()->
                new ReviewNotFoundException("Review not found by id."));
        review.setReviewer(reviewDto.getReviewer());
        review.setRating(reviewDto.getRating());
        review.setContent(review.getContent());

        Review updateReview = reviewRepository.save(review);
        return mapToDto(updateReview);
    }

    @Override
    public void deleteReview(int id) {
        Review review = reviewRepository.findById(id).orElseThrow(()->
                new ReviewNotFoundException("Review not found by id"));
        reviewRepository.delete(review);
    }

    private ReviewDto mapToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setReviewer(review.getReviewer());
        reviewDto.setContent(review.getContent());
        reviewDto.setRating(review.getRating());

        return reviewDto;
    }
}
