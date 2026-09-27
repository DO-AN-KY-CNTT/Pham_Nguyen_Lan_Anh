package com.nutribudget.api.controller;

import com.nutribudget.api.dto.request.DishRequest;
import com.nutribudget.api.dto.response.ApiResponse;
import com.nutribudget.api.dto.response.DishResponse;
import com.nutribudget.api.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DishResponse>>> getAllDishes(
            @RequestParam(required = false) String keyword) {
        List<DishResponse> response = dishService.getAllDishes(keyword);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DishResponse>> getDishById(@PathVariable Long id) {
        DishResponse response = dishService.getDishById(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DishResponse>> createDish(@RequestBody DishRequest request) {
        DishResponse response = dishService.createDish(request);
        return ResponseEntity.ok(ApiResponse.ok("Them mon an thanh cong", response));
    }
}