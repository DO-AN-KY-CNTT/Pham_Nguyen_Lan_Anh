package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class MenuCostResponse implements Serializable {
    @SerializedName("menuId")
    private Long menuId;

    @SerializedName("tenThucDon")
    private String tenThucDon;

    @SerializedName("tongChiPhi")
    private BigDecimal tongChiPhi;

    @SerializedName("nganSachNgay")
    private BigDecimal nganSachNgay;

    @SerializedName("tienConLai")
    private BigDecimal tienConLai;

    @SerializedName("soTienVuot")
    private BigDecimal soTienVuot;

    @SerializedName("tyLeSuDung")
    private Double tyLeSuDung;

    @SerializedName("chiPhiTheoBua")
    private Map<String, BigDecimal> chiPhiTheoBua;

    @SerializedName("trangThai")
    private String trangThai;

    public MenuCostResponse() {}

    public Long getMenuId() { return menuId; }
    public void setMenuId(Long menuId) { this.menuId = menuId; }

    public String getTenThucDon() { return tenThucDon; }
    public void setTenThucDon(String tenThucDon) { this.tenThucDon = tenThucDon; }

    public BigDecimal getTongChiPhi() { return tongChiPhi != null ? tongChiPhi : BigDecimal.ZERO; }
    public void setTongChiPhi(BigDecimal tongChiPhi) { this.tongChiPhi = tongChiPhi; }

    public BigDecimal getNganSachNgay() { return nganSachNgay != null ? nganSachNgay : BigDecimal.ZERO; }
    public void setNganSachNgay(BigDecimal nganSachNgay) { this.nganSachNgay = nganSachNgay; }

    public BigDecimal getTienConLai() { return tienConLai != null ? tienConLai : BigDecimal.ZERO; }
    public void setTienConLai(BigDecimal tienConLai) { this.tienConLai = tienConLai; }

    public BigDecimal getSoTienVuot() { return soTienVuot != null ? soTienVuot : BigDecimal.ZERO; }
    public void setSoTienVuot(BigDecimal soTienVuot) { this.soTienVuot = soTienVuot; }

    public Double getTyLeSuDung() { return tyLeSuDung != null ? tyLeSuDung : 0.0; }
    public void setTyLeSuDung(Double tyLeSuDung) { this.tyLeSuDung = tyLeSuDung; }

    public Map<String, BigDecimal> getChiPhiTheoBua() { return chiPhiTheoBua; }
    public void setChiPhiTheoBua(Map<String, BigDecimal> chiPhiTheoBua) { this.chiPhiTheoBua = chiPhiTheoBua; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
