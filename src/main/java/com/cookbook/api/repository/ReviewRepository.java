package com.cookbook.api.repository;

import com.cookbook.api.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> { }
