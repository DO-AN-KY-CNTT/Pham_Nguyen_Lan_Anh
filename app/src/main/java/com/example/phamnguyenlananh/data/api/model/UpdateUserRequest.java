package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;

public class UpdateUserRequest {
    @SerializedName("ho_ten")
    private String hoTen;

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

    public UpdateUserRequest() {}

    public UpdateUserRequest(String hoTen, String soDienThoai, String gioiTinh, String ngaySinh, Double chieuCao, Double canNang) {
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.chieuCao = chieuCao;
        this.canNang = canNang;
    }

    public UpdateUserRequest(String hoTen, String soDienThoai, String gioiTinh, Double chieuCao, Double canNang) {
        this(hoTen, soDienThoai, gioiTinh, null, chieuCao, canNang);
    }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

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
