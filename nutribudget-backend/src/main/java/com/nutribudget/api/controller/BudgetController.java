package com.nutribudget.api.controller;

import com.nutribudget.api.dto.request.BudgetRequest;
import com.nutribudget.api.dto.response.ApiResponse;
import com.nutribudget.api.dto.response.BudgetResponse;
import com.nutribudget.api.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<BudgetResponse>>> getBudgetsByUserId(@PathVariable Long userId) {
        List<BudgetResponse> response = budgetService.getBudgetsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BudgetResponse>> createBudget(@RequestBody BudgetRequest request) {
        BudgetResponse response = budgetService.createBudget(request);
        return ResponseEntity.ok(ApiResponse.ok("Thiet lap ngan sach thanh cong", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BudgetResponse>> updateBudget(
            @PathVariable Long id,
            @RequestBody BudgetRequest request) {
        BudgetResponse response = budgetService.updateBudget(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cap nhat ngan sach thanh cong", response));
    }
}