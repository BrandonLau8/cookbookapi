package com.cookbook.api.controllers;

import com.cookbook.api.models.Review;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class ReviewController {
    @GetMapping("reviews")
    public ResponseEntity<List<Review>> getReviews() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(1, "koala", "it was good", 5 ));
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("review")
    public Review getReview(@PathVariable int id) {
        return new Review(id, "koala", "this was good", 4);
    }

    @PostMapping("review/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }
}
