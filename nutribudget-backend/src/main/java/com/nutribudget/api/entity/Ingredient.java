package com.nutribudget.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "nguyen_lieu")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_nguyen_lieu", nullable = false, length = 150)
    private String tenNguyenLieu;

    @Column(name = "don_vi", length = 50)
    private String donVi;

    @Column(precision = 12, scale = 2)
    private BigDecimal gia;

    private Double calo;
    private Double protein;
    private Double carb;
    private Double fat;
}