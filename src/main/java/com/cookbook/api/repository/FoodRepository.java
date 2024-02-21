package com.cookbook.api.repository;

import com.cookbook.api.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//Gives methods from JPARepository also inheriting from CrudRepository and PagingSortingRepository
public interface FoodRepository extends JpaRepository<Food, Integer> {
}
