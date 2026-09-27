package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;

public class NutritionGoalResponse {
    @SerializedName("id")
    private Long id;

    @SerializedName("userId")
    private Long userId;

    @SerializedName("mucTieu")
    private String mucTieu;

    @SerializedName("caloMucTieu")
    private Double caloMucTieu;

    @SerializedName("proteinMucTieu")
    private Double proteinMucTieu;

    @SerializedName("carbMucTieu")
    private Double carbMucTieu;

    @SerializedName("fatMucTieu")
    private Double fatMucTieu;

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getMucTieu() { return mucTieu; }
    public Double getCaloMucTieu() { return caloMucTieu; }
    public Double getProteinMucTieu() { return proteinMucTieu; }
    public Double getCarbMucTieu() { return carbMucTieu; }
    public Double getFatMucTieu() { return fatMucTieu; }
}