package com.nutribudget.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "mon_an")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_mon", nullable = false, length = 150)
    private String tenMon;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "hinh_anh", length = 500)
    private String hinhAnh;

    private Double calo;
    private Double protein;
    private Double carb;
    private Double fat;

    @Column(name = "gia_du_kien", precision = 12, scale = 2)
    private BigDecimal giaDuKien;

    @Column(name = "khau_phan", length = 100)
    private String khauPhan;

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
}