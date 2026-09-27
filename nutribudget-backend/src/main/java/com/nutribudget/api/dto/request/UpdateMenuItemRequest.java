package com.nutribudget.api.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMenuItemRequest {
    @JsonProperty("mon_an_id")
    @JsonAlias({"monAnId", "dishId"})
    private Long monAnId;

    @JsonProperty("bua_an")
    @JsonAlias({"buaAn", "mealType"})
    private String buaAn;

    @JsonProperty("so_luong")
    @JsonAlias({"soLuong", "quantity"})
    private Double soLuong;
}
