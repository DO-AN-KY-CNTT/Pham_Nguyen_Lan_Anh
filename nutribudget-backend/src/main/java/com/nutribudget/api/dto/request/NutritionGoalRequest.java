package com.nutribudget.api.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class NutritionGoalRequest {
    @JsonAlias({"nguoi_dung_id", "nguoiDungId"})
    private Long userId;

    @JsonAlias({"goal", "muc_tieu"})
    private String mucTieu;

    @JsonAlias({"targetCalories", "calo_muc_tieu"})
    private Double caloMucTieu;

    @JsonAlias({"proteinGrams", "protein_muc_tieu"})
    private Double proteinMucTieu;

    @JsonAlias({"carbsGrams", "carb_muc_tieu"})
    private Double carbMucTieu;

    @JsonAlias({"fatGrams", "fat_muc_tieu"})
    private Double fatMucTieu;
}