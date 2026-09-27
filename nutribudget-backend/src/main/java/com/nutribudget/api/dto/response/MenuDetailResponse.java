package com.nutribudget.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDetailResponse {
    private Long id;
    private Long monAnId;
    private String tenMon;
    private String buaAn;
    private Double soLuong;
    private Double calo;
    private BigDecimal chiPhi;
    private String hinhAnh;
    private String khauPhan;
    private Double protein;
    private Double carb;
    private Double fat;
}
