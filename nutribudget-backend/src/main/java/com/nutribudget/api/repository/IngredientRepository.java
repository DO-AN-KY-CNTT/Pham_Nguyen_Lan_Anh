package com.nutribudget.api.repository;

import com.nutribudget.api.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByTenNguyenLieuContainingIgnoreCase(String keyword);
}