package com.cookbook.api.repository;

import com.cookbook.api.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;

//Gives methods from JPARepository also inheriting from CrudRepository and PagingSortingRepository
public interface FoodRepository extends JpaRepository<Food, Integer> {}
