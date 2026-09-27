package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;

public class BudgetResponse {
    @SerializedName("id")
    private Long id;

    @SerializedName("userId")
    private Long userId;

    @SerializedName(value = "nganSachNgay", alternate = {"ngan_sach_ngay"})
    private Double nganSachNgay;

    @SerializedName(value = "nganSachTuan", alternate = {"ngan_sach_tuan"})
    private Double nganSachTuan;

    @SerializedName(value = "nganSachThang", alternate = {"ngan_sach_thang"})
    private Double nganSachThang;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Double getNganSachNgay() { return nganSachNgay; }
    public void setNganSachNgay(Double nganSachNgay) { this.nganSachNgay = nganSachNgay; }

    public Double getNganSachTuan() { return nganSachTuan; }
    public void setNganSachTuan(Double nganSachTuan) { this.nganSachTuan = nganSachTuan; }

    public Double getNganSachThang() { return nganSachThang; }
    public void setNganSachThang(Double nganSachThang) { this.nganSachThang = nganSachThang; }
}