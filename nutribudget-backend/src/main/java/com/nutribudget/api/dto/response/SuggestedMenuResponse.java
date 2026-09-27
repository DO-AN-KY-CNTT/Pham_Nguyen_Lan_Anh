package com.nutribudget.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuggestedMenuResponse {
    private String planId;
    private String tenThucDon;
    private String moTa;
    private Integer tiLePhuHop;
    private Integer soTienTietKiem;
    private Double tongCalo;
    private Double tongProtein;
    private Double tongCarb;
    private Double tongFat;
    private BigDecimal tongChiPhi;
    private Integer soBua;
    private LocalDate ngayApDung;
    private List<MenuDetailResponse> chiTietThucDon;
}
