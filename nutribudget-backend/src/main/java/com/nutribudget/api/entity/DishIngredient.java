package com.nutribudget.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mon_an_nguyen_lieu")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DishIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mon_an_id", nullable = false)
    private Dish monAn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nguyen_lieu_id", nullable = false)
    private Ingredient nguyenLieu;

    @Column(name = "so_luong", nullable = false)
    private Double soLuong;

    @Column(name = "don_vi", length = 50)
    private String donVi;
}
