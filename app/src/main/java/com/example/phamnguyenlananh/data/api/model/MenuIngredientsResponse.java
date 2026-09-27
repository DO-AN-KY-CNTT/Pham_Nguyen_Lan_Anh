package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuIngredientsResponse implements Serializable {

    @SerializedName("menuId")
    private Long menuId;

    @SerializedName("tenThucDon")
    private String tenThucDon;

    @SerializedName("tongChiPhiNguyenLieu")
    private BigDecimal tongChiPhiNguyenLieu;

    @SerializedName("nguyenLieu")
    private List<IngredientItemResponse> nguyenLieu;

    public MenuIngredientsResponse() {
        this.nguyenLieu = new ArrayList<>();
    }

    public Long getMenuId() { return menuId; }
    public void setMenuId(Long menuId) { this.menuId = menuId; }

    public String getTenThucDon() { return tenThucDon; }
    public void setTenThucDon(String tenThucDon) { this.tenThucDon = tenThucDon; }

    public BigDecimal getTongChiPhiNguyenLieu() {
        return tongChiPhiNguyenLieu != null ? tongChiPhiNguyenLieu : BigDecimal.ZERO;
    }
    public void setTongChiPhiNguyenLieu(BigDecimal tongChiPhiNguyenLieu) {
        this.tongChiPhiNguyenLieu = tongChiPhiNguyenLieu;
    }

    public List<IngredientItemResponse> getNguyenLieu() {
        return nguyenLieu != null ? nguyenLieu : new ArrayList<>();
    }
    public void setNguyenLieu(List<IngredientItemResponse> nguyenLieu) {
        this.nguyenLieu = nguyenLieu;
    }
}
