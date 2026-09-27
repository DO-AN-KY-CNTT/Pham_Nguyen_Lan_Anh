package com.nutribudget.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ngan_sach")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private User nguoiDung;

    @Column(name = "ngan_sach_ngay", precision = 12, scale = 2)
    private BigDecimal nganSachNgay;

    @Column(name = "ngan_sach_tuan", precision = 12, scale = 2)
    private BigDecimal nganSachTuan;

    @Column(name = "ngan_sach_thang", precision = 12, scale = 2)
    private BigDecimal nganSachThang;

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