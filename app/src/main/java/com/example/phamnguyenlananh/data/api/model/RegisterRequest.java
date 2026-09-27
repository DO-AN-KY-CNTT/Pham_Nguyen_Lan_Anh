package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("ho_ten")
    private String hoTen;

    @SerializedName("email")
    private String email;

    @SerializedName("mat_khau")
    private String matKhau;

    @SerializedName("so_dien_thoai")
    private String soDienThoai;

    @SerializedName("gioi_tinh")
    private String gioiTinh;

    @SerializedName("ngay_sinh")
    private String ngaySinh;

    @SerializedName("chieu_cao")
    private Double chieuCao;

    @SerializedName("can_nang")
    private Double canNang;

    public RegisterRequest() {}

    public RegisterRequest(String hoTen, String email, String matKhau, String soDienThoai) {
        this.hoTen = hoTen;
        this.email = email;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
    }

    public RegisterRequest(String hoTen, String email, String matKhau, String soDienThoai, String gioiTinh, String ngaySinh, Double chieuCao, Double canNang) {
        this.hoTen = hoTen;
        this.email = email;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.chieuCao = chieuCao;
        this.canNang = canNang;
    }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

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
}
