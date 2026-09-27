package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.math.BigDecimal;

public class IngredientItemResponse implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("tenNguyenLieu")
    private String tenNguyenLieu;

    @SerializedName("soLuong")
    private Double soLuong;

    @SerializedName("donVi")
    private String donVi;

    @SerializedName("donGia")
    private BigDecimal donGia;

    @SerializedName("thanhTien")
    private BigDecimal thanhTien;

    private boolean isChecked;

    public IngredientItemResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTenNguyenLieu() { return tenNguyenLieu; }
    public void setTenNguyenLieu(String tenNguyenLieu) { this.tenNguyenLieu = tenNguyenLieu; }

    public Double getSoLuong() { return soLuong != null ? soLuong : 0.0; }
    public void setSoLuong(Double soLuong) { this.soLuong = soLuong; }

    public String getDonVi() { return donVi != null ? donVi : ""; }
    public void setDonVi(String donVi) { this.donVi = donVi; }

    public BigDecimal getDonGia() { return donGia != null ? donGia : BigDecimal.ZERO; }
    public void setDonGia(BigDecimal donGia) { this.donGia = donGia; }

    public BigDecimal getThanhTien() { return thanhTien != null ? thanhTien : BigDecimal.ZERO; }
    public void setThanhTien(BigDecimal thanhTien) { this.thanhTien = thanhTien; }

    public boolean isChecked() { return isChecked; }
    public void setChecked(boolean checked) { isChecked = checked; }
}
