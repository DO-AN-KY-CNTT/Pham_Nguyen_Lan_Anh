-- ====================================================================
-- NutriBudget Database Schema (MySQL 8 / MariaDB compatible)
-- ====================================================================

-- 1. Báº£ng nguoi_dung
CREATE TABLE IF NOT EXISTS nguoi_dung (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ho_ten VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    mat_khau VARCHAR(255) NOT NULL,
    so_dien_thoai VARCHAR(20),
    gioi_tinh VARCHAR(20),
    ngay_sinh DATE,
    chieu_cao DOUBLE,
    can_nang DOUBLE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Báº£ng muc_tieu_dinh_duong
CREATE TABLE IF NOT EXISTS muc_tieu_dinh_duong (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nguoi_dung_id BIGINT NOT NULL,
    muc_tieu VARCHAR(100),
    calo_muc_tieu DOUBLE,
    protein_muc_tieu DOUBLE,
    carb_muc_tieu DOUBLE,
    fat_muc_tieu DOUBLE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_muc_tieu_nguoi_dung FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Báº£ng ngan_sach
CREATE TABLE IF NOT EXISTS ngan_sach (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nguoi_dung_id BIGINT NOT NULL,
    ngan_sach_ngay DECIMAL(12,2),
    ngan_sach_tuan DECIMAL(12,2),
    ngan_sach_thang DECIMAL(12,2),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_ngan_sach_nguoi_dung FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Báº£ng mon_an
CREATE TABLE IF NOT EXISTS mon_an (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ten_mon VARCHAR(150) NOT NULL,
    mo_ta TEXT,
    hinh_anh VARCHAR(500),
    calo DOUBLE,
    protein DOUBLE,
    carb DOUBLE,
    fat DOUBLE,
    gia_du_kien DECIMAL(12,2),
    khau_phan VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Báº£ng nguyen_lieu
CREATE TABLE IF NOT EXISTS nguyen_lieu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ten_nguyen_lieu VARCHAR(150) NOT NULL,
    don_vi VARCHAR(50),
    gia DECIMAL(12,2),
    calo DOUBLE,
    protein DOUBLE,
    carb DOUBLE,
    fat DOUBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Báº£ng thuc_don
CREATE TABLE IF NOT EXISTS thuc_don (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nguoi_dung_id BIGINT NOT NULL,
    ten_thuc_don VARCHAR(150),
    ngay_ap_dung DATE,
    tong_calo DOUBLE,
    tong_protein DOUBLE,
    tong_carb DOUBLE,
    tong_fat DOUBLE,
    tong_chi_phi DECIMAL(12,2),
    trang_thai VARCHAR(50) DEFAULT 'HOAT_DONG',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_thuc_don_nguoi_dung FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Báº£ng chi_tiet_thuc_don
CREATE TABLE IF NOT EXISTS chi_tiet_thuc_don (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    thuc_don_id BIGINT NOT NULL,
    mon_an_id BIGINT NOT NULL,
    bua_an VARCHAR(50),
    so_luong DOUBLE DEFAULT 1.0,
    calo DOUBLE,
    chi_phi DECIMAL(12,2),
    CONSTRAINT fk_chi_tiet_thuc_don FOREIGN KEY (thuc_don_id) REFERENCES thuc_don(id) ON DELETE CASCADE,
    CONSTRAINT fk_chi_tiet_mon_an FOREIGN KEY (mon_an_id) REFERENCES mon_an(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Báº£ng lich_su_thuc_don
CREATE TABLE IF NOT EXISTS lich_su_thuc_don (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nguoi_dung_id BIGINT NOT NULL,
    thuc_don_id BIGINT,
    hanh_dong VARCHAR(100),
    thoi_gian DATETIME DEFAULT CURRENT_TIMESTAMP,
    ghi_chu TEXT,
    CONSTRAINT fk_lich_su_nguoi_dung FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung(id) ON DELETE CASCADE,
    CONSTRAINT fk_lich_su_thuc_don FOREIGN KEY (thuc_don_id) REFERENCES thuc_don(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;