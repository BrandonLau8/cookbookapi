package com.cookbook.api.controllers;

import com.cookbook.api.dto.ReviewDto;
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

    @GetMapping("reviews")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return new ResponseEntity<>(reviewService.getAllReviews(), HttpStatus.OK);
    }

    @GetMapping("review/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable("id") int reviewId) {
        return new ResponseEntity<>(reviewService.getReviewById(reviewId), HttpStatus.OK);
    }

    @PostMapping("review/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto) {
        return new ResponseEntity<>(reviewService.createReview(reviewDto), HttpStatus.CREATED);
    }

    @PutMapping("review/{id}/update")
    public ResponseEntity<ReviewDto> updateReview(@RequestBody ReviewDto reviewDto, @PathVariable("id") int reviewId) {
        ReviewDto reviewresponse = reviewService.updateReview(reviewDto, reviewId);
        return new ResponseEntity<>(reviewresponse, HttpStatus.OK);
    }

    @DeleteMapping("review/{id}/delete")
    public ResponseEntity<String> deleteReview(@PathVariable("id") int reviewId) {
        reviewService.deleteReview(reviewId);
        return new ResponseEntity<>("Review Deleted", HttpStatus.OK);
    }
}
