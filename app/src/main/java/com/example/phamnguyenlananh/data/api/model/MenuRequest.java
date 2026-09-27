package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MenuRequest {
    @SerializedName("userId")
    private Long userId;

    @SerializedName("tenThucDon")
    private String tenThucDon;

    @SerializedName("ngayApDung")
    private String ngayApDung;

    @SerializedName("trangThai")
    private String trangThai;

    @SerializedName("items")
    private List<MenuDetailRequest> items;

    public MenuRequest() {}

    public MenuRequest(Long userId, String tenThucDon, String ngayApDung, String trangThai, List<MenuDetailRequest> items) {
        this.userId = userId;
        this.tenThucDon = tenThucDon;
        this.ngayApDung = ngayApDung;
        this.trangThai = trangThai;
        this.items = items;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTenThucDon() { return tenThucDon; }
    public void setTenThucDon(String tenThucDon) { this.tenThucDon = tenThucDon; }

    public String getNgayApDung() { return ngayApDung; }
    public void setNgayApDung(String ngayApDung) { this.ngayApDung = ngayApDung; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public List<MenuDetailRequest> getItems() { return items; }
    public void setItems(List<MenuDetailRequest> items) { this.items = items; }
}
