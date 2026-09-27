package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.math.BigDecimal;

public class MenuHistoryResponse implements Serializable {
    @SerializedName("id")
    private Long id;

    @SerializedName("userId")
    private Long userId;

    @SerializedName("thucDonId")
    private Long thucDonId;

    @SerializedName("tenThucDon")
    private String tenThucDon;

    @SerializedName("ngayApDung")
    private String ngayApDung;

    @SerializedName("hanhDong")
    private String hanhDong;

    @SerializedName("thoiGian")
    private String thoiGian;

    @SerializedName("tongCalo")
    private Double tongCalo;

    @SerializedName("tongChiPhi")
    private BigDecimal tongChiPhi;

    @SerializedName("trangThai")
    private String trangThai;

    @SerializedName("ghiChu")
    private String ghiChu;

    public MenuHistoryResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getThucDonId() { return thucDonId; }
    public void setThucDonId(Long thucDonId) { this.thucDonId = thucDonId; }

    public String getTenThucDon() { return tenThucDon; }
    public void setTenThucDon(String tenThucDon) { this.tenThucDon = tenThucDon; }

    public String getNgayApDung() { return ngayApDung; }
    public void setNgayApDung(String ngayApDung) { this.ngayApDung = ngayApDung; }

    public String getHanhDong() { return hanhDong; }
    public void setHanhDong(String hanhDong) { this.hanhDong = hanhDong; }

    public String getThoiGian() { return thoiGian; }
    public void setThoiGian(String thoiGian) { this.thoiGian = thoiGian; }

    public Double getTongCalo() { return tongCalo != null ? tongCalo : 0.0; }
    public void setTongCalo(Double tongCalo) { this.tongCalo = tongCalo; }

    public BigDecimal getTongChiPhi() { return tongChiPhi != null ? tongChiPhi : BigDecimal.ZERO; }
    public void setTongChiPhi(BigDecimal tongChiPhi) { this.tongChiPhi = tongChiPhi; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}