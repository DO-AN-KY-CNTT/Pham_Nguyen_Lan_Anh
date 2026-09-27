package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;

public class MenuDetailRequest {
    @SerializedName("monAnId")
    private Long monAnId;

    @SerializedName("buaAn")
    private String buaAn;

    @SerializedName("soLuong")
    private Double soLuong;

    @SerializedName("calo")
    private Double calo;

    @SerializedName("chiPhi")
    private Double chiPhi;

    public MenuDetailRequest() {}

    public MenuDetailRequest(Long monAnId, String buaAn, Double soLuong, Double calo, Double chiPhi) {
        this.monAnId = monAnId;
        this.buaAn = buaAn;
        this.soLuong = soLuong;
        this.calo = calo;
        this.chiPhi = chiPhi;
    }

    public Long getMonAnId() { return monAnId; }
    public void setMonAnId(Long monAnId) { this.monAnId = monAnId; }

    public String getBuaAn() { return buaAn; }
    public void setBuaAn(String buaAn) { this.buaAn = buaAn; }

    public Double getSoLuong() { return soLuong; }
    public void setSoLuong(Double soLuong) { this.soLuong = soLuong; }

    public Double getCalo() { return calo; }
    public void setCalo(Double calo) { this.calo = calo; }

    public Double getChiPhi() { return chiPhi; }
    public void setChiPhi(Double chiPhi) { this.chiPhi = chiPhi; }
}
