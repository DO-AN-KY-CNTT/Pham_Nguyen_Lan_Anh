package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;

public class MenuDetailResponse {
    @SerializedName("id")
    private Long id;

    @SerializedName("monAnId")
    private Long monAnId;

    @SerializedName("tenMon")
    private String tenMon;

    @SerializedName("buaAn")
    private String buaAn;

    @SerializedName("soLuong")
    private Double soLuong;

    @SerializedName("calo")
    private Double calo;

    @SerializedName("chiPhi")
    private Double chiPhi;

    @SerializedName("hinhAnh")
    private String hinhAnh;

    @SerializedName("khauPhan")
    private String khauPhan;

    @SerializedName("protein")
    private Double protein;

    @SerializedName("carb")
    private Double carb;

    @SerializedName("fat")
    private Double fat;

    public Long getId() { return id; }
    public Long getMonAnId() { return monAnId; }
    public String getTenMon() { return tenMon; }
    public String getBuaAn() { return buaAn; }
    public Double getSoLuong() { return soLuong; }
    public Double getCalo() { return calo; }
    public Double getChiPhi() { return chiPhi; }
    public String getHinhAnh() { return hinhAnh; }
    public String getKhauPhan() { return khauPhan; }
    public Double getProtein() { return protein; }
    public Double getCarb() { return carb; }
    public Double getFat() { return fat; }
}
