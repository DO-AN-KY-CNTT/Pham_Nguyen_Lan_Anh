package com.nutribudget.api.controller;

import com.nutribudget.api.dto.request.NutritionGoalRequest;
import com.nutribudget.api.dto.response.ApiResponse;
import com.nutribudget.api.dto.response.NutritionGoalResponse;
import com.nutribudget.api.service.NutritionGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nutrition-goals")
public class NutritionGoalController {

    @Autowired
    private NutritionGoalService goalService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NutritionGoalResponse>>> getGoalsByUserId(@PathVariable Long userId) {
        List<NutritionGoalResponse> response = goalService.getGoalsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NutritionGoalResponse>> createGoal(@RequestBody NutritionGoalRequest request) {
        NutritionGoalResponse response = goalService.createGoal(request);
        return ResponseEntity.ok(ApiResponse.ok("Thiet lap muc tieu dinh duong thanh cong", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NutritionGoalResponse>> updateGoal(
            @PathVariable Long id,
            @RequestBody NutritionGoalRequest request) {
        NutritionGoalResponse response = goalService.updateGoal(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cap nhat muc tieu dinh duong thanh cong", response));
    }
}