package com.nutribudget.api.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class MenuDetailRequest {
    @JsonAlias({"dishId", "mon_an_id"})
    private Long monAnId;

    @JsonAlias({"mealType", "bua_an"})
    private String buaAn; // SANG, TRUA, TOI, PHU

    @JsonAlias({"quantity", "so_luong"})
    private Double soLuong = 1.0;

    private Double calo;

    @JsonAlias({"cost", "chi_phi"})
    private BigDecimal chiPhi;
}