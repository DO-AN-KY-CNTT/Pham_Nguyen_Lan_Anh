package com.nutribudget.api.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class BudgetRequest {
    @JsonAlias({"nguoi_dung_id", "nguoiDungId"})
    private Long userId;

    @JsonAlias({"dailyBudget", "ngan_sach_ngay"})
    private BigDecimal nganSachNgay;

    @JsonAlias({"weeklyBudget", "ngan_sach_tuan"})
    private BigDecimal nganSachTuan;

    @JsonAlias({"monthlyBudget", "ngan_sach_thang"})
    private BigDecimal nganSachThang;
}