package com.nutribudget.api.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientItemResponse {
    private Long id;
    private String tenNguyenLieu;
    private Double soLuong;
    private String donVi;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
}
