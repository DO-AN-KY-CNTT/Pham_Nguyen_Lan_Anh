package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MenuResponse {
    @SerializedName("id")
    private Long id;

    @SerializedName("userId")
    private Long userId;

    @SerializedName("tenThucDon")
    private String tenThucDon;

    @SerializedName("ngayApDung")
    private String ngayApDung;

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

    @SerializedName("trangThai")
    private String trangThai;

    @SerializedName("chiTietThucDon")
    private List<MenuDetailResponse> chiTietThucDon;

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getTenThucDon() { return tenThucDon; }
    public String getNgayApDung() { return ngayApDung; }
    public Double getTongCalo() { return tongCalo; }
    public Double getTongProtein() { return tongProtein; }
    public Double getTongCarb() { return tongCarb; }
    public Double getTongFat() { return tongFat; }
    public Double getTongChiPhi() { return tongChiPhi; }
    public String getTrangThai() { return trangThai; }
    public List<MenuDetailResponse> getChiTietThucDon() { return chiTietThucDon; }
}