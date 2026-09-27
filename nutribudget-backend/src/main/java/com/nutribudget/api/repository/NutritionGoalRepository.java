package com.nutribudget.api.repository;

import com.nutribudget.api.entity.NutritionGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NutritionGoalRepository extends JpaRepository<NutritionGoal, Long> {
    List<NutritionGoal> findByNguoiDungId(Long userId);
    Optional<NutritionGoal> findTopByNguoiDungIdOrderByCreatedAtDesc(Long userId);
}