package com.nutribudget.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "thuc_don")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private User nguoiDung;

    @Column(name = "ten_thuc_don", length = 150)
    private String tenThucDon;

    @Column(name = "ngay_ap_dung")
    private LocalDate ngayApDung;

    @Column(name = "tong_calo")
    private Double tongCalo;

    @Column(name = "tong_protein")
    private Double tongProtein;

    @Column(name = "tong_carb")
    private Double tongCarb;

    @Column(name = "tong_fat")
    private Double tongFat;

    @Column(name = "tong_chi_phi", precision = 12, scale = 2)
    private BigDecimal tongChiPhi;

    @Column(name = "trang_thai", length = 50)
    @Builder.Default
    private String trangThai = "HOAT_DONG";

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "thucDon", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MenuDetail> chiTietThucDon = new ArrayList<>();
}