package com.nutribudget.api.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DishRequest {
    @JsonAlias({"name", "ten_mon"})
    private String tenMon;

    @JsonAlias({"description", "mo_ta"})
    private String moTa;

    @JsonAlias({"imageUrl", "hinh_anh"})
    private String hinhAnh;

    private Double calo;
    private Double protein;
    private Double carb;
    private Double fat;

    @JsonAlias({"cost", "gia_du_kien"})
    private BigDecimal giaDuKien;

    @JsonAlias({"portion", "khau_phan"})
    private String khauPhan;
}