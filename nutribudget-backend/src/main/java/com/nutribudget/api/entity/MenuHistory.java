package com.nutribudget.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lich_su_thuc_don")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MenuHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private User nguoiDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thuc_don_id")
    private Menu thucDon;

    @Column(name = "hanh_dong", length = 100)
    private String hanhDong;

    @Column(name = "thoi_gian")
    @Builder.Default
    private LocalDateTime thoiGian = LocalDateTime.now();

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;
}