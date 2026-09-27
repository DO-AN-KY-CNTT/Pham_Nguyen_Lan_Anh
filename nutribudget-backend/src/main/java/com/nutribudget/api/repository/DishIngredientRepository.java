package com.nutribudget.api.repository;

import com.nutribudget.api.entity.DishIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishIngredientRepository extends JpaRepository<DishIngredient, Long> {
    List<DishIngredient> findByMonAnId(Long monAnId);
    List<DishIngredient> findByMonAnIdIn(List<Long> monAnIds);
}
