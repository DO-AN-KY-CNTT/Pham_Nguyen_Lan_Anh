package com.nutribudget.api.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    @JsonAlias({"fullName", "ho_ten"})
    private String hoTen;

    @JsonAlias({"phone", "so_dien_thoai"})
    private String soDienThoai;

    @JsonAlias({"gender", "gioi_tinh"})
    private String gioiTinh;

    @JsonAlias({"birthDate", "ngay_sinh"})
    private LocalDate ngaySinh;

    @JsonAlias({"height", "heightCm", "chieu_cao"})
    private Double chieuCao;

    @JsonAlias({"weight", "weightKg", "can_nang"})
    private Double canNang;
}