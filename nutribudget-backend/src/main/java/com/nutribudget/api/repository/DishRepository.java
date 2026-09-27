package com.nutribudget.api.repository;

import com.nutribudget.api.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByTenMonContainingIgnoreCase(String keyword);
}