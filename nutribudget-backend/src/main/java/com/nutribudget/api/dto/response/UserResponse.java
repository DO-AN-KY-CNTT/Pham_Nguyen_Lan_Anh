package com.nutribudget.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserResponse {
    private Long id;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String gioiTinh;
    private LocalDate ngaySinh;
    private Double chieuCao;
    private Double canNang;
    private Double bmi;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Fallback getters for compatibility
    public String getFullName() { return hoTen; }
    public String getPhone() { return soDienThoai; }
    public String getGender() { return gioiTinh; }
}