package com.nutribudget.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuCostResponse {
    private Long menuId;
    private String tenThucDon;
    private BigDecimal tongChiPhi;
    private BigDecimal nganSachNgay;
    private BigDecimal tienConLai;
    private BigDecimal soTienVuot;
    private Double tyLeSuDung;
    private Map<String, BigDecimal> chiPhiTheoBua;
    private String trangThai; // TRONG_NGAN_SACH, VUOT_NGAN_SACH
}
