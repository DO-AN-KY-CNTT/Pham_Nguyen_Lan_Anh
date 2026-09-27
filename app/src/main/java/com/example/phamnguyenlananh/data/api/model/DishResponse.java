package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;

public class DishResponse {
    @SerializedName("id")
    private Long id;

    @SerializedName("tenMon")
    private String tenMon;

    @SerializedName("moTa")
    private String moTa;

    @SerializedName("hinhAnh")
    private String hinhAnh;

    @SerializedName("calo")
    private Double calo;

    @SerializedName("protein")
    private Double protein;

    @SerializedName("carb")
    private Double carb;

    @SerializedName("fat")
    private Double fat;

    @SerializedName("giaDuKien")
    private Double giaDuKien;

    @SerializedName("khauPhan")
    private String khauPhan;

    public Long getId() { return id; }
    public String getTenMon() { return tenMon; }
    public String getMoTa() { return moTa; }
    public String getHinhAnh() { return hinhAnh; }
    public Double getCalo() { return calo; }
    public Double getProtein() { return protein; }
    public Double getCarb() { return carb; }
    public Double getFat() { return fat; }
    public Double getGiaDuKien() { return giaDuKien; }
    public String getKhauPhan() { return khauPhan; }
}