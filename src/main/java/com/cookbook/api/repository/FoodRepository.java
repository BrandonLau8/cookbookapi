package com.cookbook.api.repository;

import com.cookbook.api.dto.FoodDto;
import com.cookbook.api.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

//Gives methods from JPARepository also inheriting from CrudRepository and PagingSortingRepository
public interface FoodRepository extends JpaRepository<Food, Integer> {
//    @Query("SELECT f FROM Food f WHERE" +
//            "f.name LIKE CONCAT('%',:query,'%')")
//    List<Food> searchFoods(String query);
}
