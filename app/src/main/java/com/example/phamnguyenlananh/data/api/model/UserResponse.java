package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("id")
    private Long id;

    @SerializedName("hoTen")
    private String hoTen;

    @SerializedName("email")
    private String email;

    @SerializedName("soDienThoai")
    private String soDienThoai;

    @SerializedName("gioiTinh")
    private String gioiTinh;

    @SerializedName("ngaySinh")
    private String ngaySinh;

    @SerializedName("chieuCao")
    private Double chieuCao;

    @SerializedName("canNang")
    private Double canNang;

    @SerializedName("bmi")
    private Double bmi;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }

    public Double getChieuCao() { return chieuCao; }
    public void setChieuCao(Double chieuCao) { this.chieuCao = chieuCao; }

    public Double getCanNang() { return canNang; }
    public void setCanNang(Double canNang) { this.canNang = canNang; }

    public Double getBmi() { return bmi; }
    public void setBmi(Double bmi) { this.bmi = bmi; }
}