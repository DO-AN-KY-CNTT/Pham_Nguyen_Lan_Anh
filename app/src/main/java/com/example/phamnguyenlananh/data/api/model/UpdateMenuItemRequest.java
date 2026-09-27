package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UpdateMenuItemRequest implements Serializable {
    @SerializedName("mon_an_id")
    private Long monAnId;

    @SerializedName("bua_an")
    private String buaAn;

    @SerializedName("so_luong")
    private Double soLuong;

    public UpdateMenuItemRequest() {}

    public UpdateMenuItemRequest(Long monAnId, String buaAn, Double soLuong) {
        this.monAnId = monAnId;
        this.buaAn = buaAn;
        this.soLuong = soLuong;
    }

    public Long getMonAnId() { return monAnId; }
    public void setMonAnId(Long monAnId) { this.monAnId = monAnId; }

    public String getBuaAn() { return buaAn; }
    public void setBuaAn(String buaAn) { this.buaAn = buaAn; }

    public Double getSoLuong() { return soLuong; }
    public void setSoLuong(Double soLuong) { this.soLuong = soLuong; }
}
