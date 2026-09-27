package com.nutribudget.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "nguoi_dung")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "email", unique = true, nullable = false, length = 150)
    private String email;

    @Column(name = "mat_khau", nullable = false, length = 255)
    private String matKhau;

    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    @Column(name = "gioi_tinh", length = 20)
    private String gioiTinh;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "chieu_cao")
    private Double chieuCao;

    @Column(name = "can_nang")
    private Double canNang;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Double getBmi() {
        if (chieuCao != null && canNang != null && chieuCao > 0) {
            double h = chieuCao / 100.0;
            return Math.round((canNang / (h * h)) * 10.0) / 10.0;
        }
        return null;
    }

    public String getPasswordHash() {
        return matKhau;
    }

    public String getRole() {
        return "USER";
    }
}