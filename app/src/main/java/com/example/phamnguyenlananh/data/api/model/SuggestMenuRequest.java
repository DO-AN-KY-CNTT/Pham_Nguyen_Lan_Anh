package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;

public class SuggestMenuRequest {
    @SerializedName("userId")
    private Long userId;

    @SerializedName("ngayApDung")
    private String ngayApDung;

    @SerializedName("soBua")
    private Integer soBua;

    @SerializedName("tuyChon")
    private String tuyChon;

    public SuggestMenuRequest() {}

    public SuggestMenuRequest(Long userId, String ngayApDung, Integer soBua, String tuyChon) {
        this.userId = userId;
        this.ngayApDung = ngayApDung;
        this.soBua = soBua;
        this.tuyChon = tuyChon;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getNgayApDung() { return ngayApDung; }
    public void setNgayApDung(String ngayApDung) { this.ngayApDung = ngayApDung; }

    public Integer getSoBua() { return soBua; }
    public void setSoBua(Integer soBua) { this.soBua = soBua; }

    public String getTuyChon() { return tuyChon; }
    public void setTuyChon(String tuyChon) { this.tuyChon = tuyChon; }
}
