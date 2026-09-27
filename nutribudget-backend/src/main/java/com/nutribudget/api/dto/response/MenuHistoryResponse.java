package com.nutribudget.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MenuHistoryResponse {
    private Long id;
    private Long userId;
    private Long thucDonId;
    private String tenThucDon;
    private String ngayApDung;
    private String hanhDong;
    private LocalDateTime thoiGian;
    private Double tongCalo;
    private BigDecimal tongChiPhi;
    private String trangThai;
    private String ghiChu;
}