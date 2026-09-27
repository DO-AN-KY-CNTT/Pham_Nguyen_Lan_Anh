package com.nutribudget.api.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class MenuRequest {
    @JsonAlias({"nguoi_dung_id", "nguoiDungId"})
    private Long userId;

    @JsonAlias({"name", "planName", "ten_thuc_don"})
    private String tenThucDon;

    @JsonAlias({"planDate", "date", "ngay_ap_dung"})
    private LocalDate ngayApDung;

    @JsonAlias({"status", "trang_thai"})
    private String trangThai = "HOAT_DONG";

    @JsonAlias({"details", "chiTietThucDon"})
    private List<MenuDetailRequest> items;
}