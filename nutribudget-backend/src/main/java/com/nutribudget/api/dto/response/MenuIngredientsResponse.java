package com.nutribudget.api.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuIngredientsResponse {
    private Long menuId;
    private String tenThucDon;
    private BigDecimal tongChiPhiNguyenLieu;
    private List<IngredientItemResponse> nguyenLieu;
}
