package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;

public class NutritionGoalRequest {
    @SerializedName("userId")
    private Long userId;

    @SerializedName("muc_tieu")
    private String mucTieu;

    @SerializedName("calo_muc_tieu")
    private Double caloMucTieu;

    @SerializedName("protein_muc_tieu")
    private Double proteinMucTieu;

    @SerializedName("carb_muc_tieu")
    private Double carbMucTieu;

    @SerializedName("fat_muc_tieu")
    private Double fatMucTieu;

    public NutritionGoalRequest(Long userId, String mucTieu, Double caloMucTieu, Double proteinMucTieu, Double carbMucTieu, Double fatMucTieu) {
        this.userId = userId;
        this.mucTieu = mucTieu;
        this.caloMucTieu = caloMucTieu;
        this.proteinMucTieu = proteinMucTieu;
        this.carbMucTieu = carbMucTieu;
        this.fatMucTieu = fatMucTieu;
    }

    public Long getUserId() { return userId; }
    public String getMucTieu() { return mucTieu; }
    public Double getCaloMucTieu() { return caloMucTieu; }
    public Double getProteinMucTieu() { return proteinMucTieu; }
    public Double getCarbMucTieu() { return carbMucTieu; }
    public Double getFatMucTieu() { return fatMucTieu; }
}