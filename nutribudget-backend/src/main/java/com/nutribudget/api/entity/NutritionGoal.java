package com.nutribudget.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "muc_tieu_dinh_duong")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NutritionGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private User nguoiDung;

    @Column(name = "muc_tieu", length = 100)
    private String mucTieu;

    @Column(name = "calo_muc_tieu")
    private Double caloMucTieu;

    @Column(name = "protein_muc_tieu")
    private Double proteinMucTieu;

    @Column(name = "carb_muc_tieu")
    private Double carbMucTieu;

    @Column(name = "fat_muc_tieu")
    private Double fatMucTieu;

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