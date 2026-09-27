package com.nutribudget.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DishResponse {
    private Long id;
    private String tenMon;
    private String moTa;
    private String hinhAnh;
    private Double calo;
    private Double protein;
    private Double carb;
    private Double fat;
    private BigDecimal giaDuKien;
    private String khauPhan;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Fallback getters for compatibility
    public String getName() { return tenMon; }
    public String getDescription() { return moTa; }
    public String getImageUrl() { return hinhAnh; }
    public String getPortion() { return khauPhan; }
    public Integer getCost() { return giaDuKien != null ? giaDuKien.intValue() : 0; }
}