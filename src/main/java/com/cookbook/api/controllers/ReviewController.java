package com.cookbook.api.controllers;

import com.cookbook.api.dto.ReviewDto;
import com.cookbook.api.dto.ReviewResponse;
import com.cookbook.api.models.Review;
import com.cookbook.api.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class ReviewController {
    public ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("{foodId}/reviews")
    public ResponseEntity<ReviewResponse> getAllReviews(
            @PathVariable(value = "foodId") int foodId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(reviewService.getReviewsByFoodId(foodId, pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("{foodId}/review/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewById(
            @PathVariable("foodId") int foodId,
            @PathVariable("reviewId") int reviewId
    ) {
        return new ResponseEntity<>(reviewService.getReviewById(foodId, reviewId), HttpStatus.OK);
    }

    @PostMapping("{foodId}/review")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable(value = "foodId") int foodId,
            @RequestBody ReviewDto reviewDto) {
        return new ResponseEntity<>(reviewService.createReview(foodId, reviewDto), HttpStatus.CREATED);
    }

    @PutMapping("{foodId}/review/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable("foodId") int foodId,
            @RequestBody ReviewDto reviewDto,
            @PathVariable("reviewId") int reviewId
    ) {
        ReviewDto reviewresponse = reviewService.updateReview(foodId, reviewDto, reviewId);
        return new ResponseEntity<>(reviewresponse, HttpStatus.OK);
    }

    @DeleteMapping("{foodId}/review/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable("foodId") int foodId,
            @PathVariable("reviewId") int reviewId
    ) {
        reviewService.deleteReview(foodId, reviewId);
        return new ResponseEntity<>("Review Deleted", HttpStatus.OK);
    }
}
