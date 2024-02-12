package com.cookbook.api.repository;

import com.cookbook.api.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Slice<Review> findByFoodId(int id, Pageable pageable);
}
