package com.nutribudget.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "chi_tiet_thuc_don")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MenuDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thuc_don_id", nullable = false)
    private Menu thucDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mon_an_id", nullable = false)
    private Dish monAn;

    @Column(name = "bua_an", length = 50)
    private String buaAn; // SANG, TRUA, TOI, PHU

    @Column(name = "so_luong")
    @Builder.Default
    private Double soLuong = 1.0;

    private Double calo;

    @Column(name = "chi_phi", precision = 12, scale = 2)
    private BigDecimal chiPhi;
}