package com.nutribudget.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MenuResponse {
    private Long id;
    private Long userId;
    private String tenThucDon;
    private LocalDate ngayApDung;
    private Double tongCalo;
    private Double tongProtein;
    private Double tongCarb;
    private Double tongFat;
    private BigDecimal tongChiPhi;
    private String trangThai;
    private LocalDateTime createdAt;
    private List<MenuDetailResponse> chiTietThucDon;
}