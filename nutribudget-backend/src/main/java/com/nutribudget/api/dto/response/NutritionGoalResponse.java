package com.nutribudget.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NutritionGoalResponse {
    private Long id;
    private Long userId;
    private String mucTieu;
    private Double caloMucTieu;
    private Double proteinMucTieu;
    private Double carbMucTieu;
    private Double fatMucTieu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}