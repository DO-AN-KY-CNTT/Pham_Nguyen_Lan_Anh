package com.nutribudget.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BudgetResponse {
    private Long id;
    private Long userId;
    private BigDecimal nganSachNgay;
    private BigDecimal nganSachTuan;
    private BigDecimal nganSachThang;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}