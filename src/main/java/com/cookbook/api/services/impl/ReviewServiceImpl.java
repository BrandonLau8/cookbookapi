package com.cookbook.api.services.impl;

import com.cookbook.api.dto.ReviewDto;
import com.cookbook.api.dto.ReviewResponse;
import com.cookbook.api.exceptions.FoodNotFoundException;
import com.cookbook.api.exceptions.ReviewNotFoundException;
import com.cookbook.api.models.Food;
import com.cookbook.api.models.Review;
import com.cookbook.api.repository.FoodRepository;
import com.cookbook.api.repository.ReviewRepository;
import com.cookbook.api.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private FoodRepository foodRepository;
    private ReviewRepository reviewRepository;
    @Autowired
    public ReviewServiceImpl(FoodRepository foodRepository, ReviewRepository reviewRepository) {
        this.foodRepository = foodRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public ReviewDto createReview(int foodId, ReviewDto reviewDto) {
        Review review = mapToEntity(reviewDto);
        Food food = foodRepository.findById(foodId).orElseThrow(()->
                new FoodNotFoundException("Food not found by id."));

        review.setFood(food);

        Review newReview = reviewRepository.save(review);

        return mapToDto(newReview);
    }

    @Override
    public ReviewResponse getReviewsByFoodId(int foodId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Slice<Review> reviews = reviewRepository.findByFoodId(foodId, pageable);
        List<Review> listOfReviews = reviews.getContent();
        List<ReviewDto> content = listOfReviews.stream().map((x)->mapToDto(x)).collect(Collectors.toList());

        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setContent(content);
        reviewResponse.setPageNo(reviews.getNumber());
        reviewResponse.setPageSize(reviews.getSize());
        reviewResponse.setTotalElements(reviews.getNumberOfElements());
        reviewResponse.setLast(reviews.isLast());

        return reviewResponse;
    }

    @Override
    public ReviewDto getReviewById(int foodId, int reviewId) {
        Food food = foodRepository.findById(foodId).orElseThrow(()->
                new FoodNotFoundException("Food not found by id."));
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->
                new ReviewNotFoundException("Review not found by Id"));

        if(review.getFood().getId() != food.getId()) {
            throw new FoodNotFoundException("Review does not belong with food.");
        }

        return mapToDto(review);
    }

    @Override
    public ReviewDto updateReview(int foodId, ReviewDto reviewDto, int reviewId) {
        Food food = foodRepository.findById(foodId).orElseThrow(()->
                new FoodNotFoundException("Food not found by id,"));
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->
                new ReviewNotFoundException("Review not found by id."));

        if(review.getFood().getId() != food.getId()) {
            throw new FoodNotFoundException("Review does not belong to food.");
        }

        review.setReviewer(reviewDto.getReviewer());
        review.setRating(reviewDto.getRating());
        review.setComment(review.getComment());

        Review updateReview = reviewRepository.save(review);
        return mapToDto(updateReview);
    }

    @Override
    public void deleteReview(int foodId, int reviewId) {
        Food food = foodRepository.findById(foodId).orElseThrow(()->
                new FoodNotFoundException("Food not found by id."));
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->
                new ReviewNotFoundException("Review not found by id"));

        if(review.getFood().getId() != food.getId()) {
            throw new FoodNotFoundException("Review does not belong to food.");
        }
        reviewRepository.delete(review);
    }

    private ReviewDto mapToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setReviewer(review.getReviewer());
        reviewDto.setComment(review.getComment());
        reviewDto.setRating(review.getRating());

        return reviewDto;
    }

    private Review mapToEntity(ReviewDto reviewDto) {
        Review review = new Review();
        review.setId(reviewDto.getId());
        review.setReviewer(reviewDto.getReviewer());
        review.setComment(reviewDto.getComment());
        review.setRating(reviewDto.getRating());

        return review;
    }
}
