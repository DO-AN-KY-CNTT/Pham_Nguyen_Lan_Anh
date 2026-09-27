package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SuggestedMenuResponse {
    @SerializedName("planId")
    private String planId;

    @SerializedName("tenThucDon")
    private String tenThucDon;

    @SerializedName("moTa")
    private String moTa;

    @SerializedName("tiLePhuHop")
    private Integer tiLePhuHop;

    @SerializedName("soTienTietKiem")
    private Integer soTienTietKiem;

    @SerializedName("tongCalo")
    private Double tongCalo;

    @SerializedName("tongProtein")
    private Double tongProtein;

    @SerializedName("tongCarb")
    private Double tongCarb;

    @SerializedName("tongFat")
    private Double tongFat;

    @SerializedName("tongChiPhi")
    private Double tongChiPhi;

    @SerializedName("soBua")
    private Integer soBua;

    @SerializedName("ngayApDung")
    private String ngayApDung;

    @SerializedName("chiTietThucDon")
    private List<MenuDetailResponse> chiTietThucDon;

    public String getPlanId() { return planId; }
    public String getTenThucDon() { return tenThucDon; }
    public String getMoTa() { return moTa; }
    public Integer getTiLePhuHop() { return tiLePhuHop; }
    public Integer getSoTienTietKiem() { return soTienTietKiem; }
    public Double getTongCalo() { return tongCalo; }
    public Double getTongProtein() { return tongProtein; }
    public Double getTongCarb() { return tongCarb; }
    public Double getTongFat() { return tongFat; }
    public Double getTongChiPhi() { return tongChiPhi; }
    public Integer getSoBua() { return soBua; }
    public String getNgayApDung() { return ngayApDung; }
    public List<MenuDetailResponse> getChiTietThucDon() { return chiTietThucDon; }
}
