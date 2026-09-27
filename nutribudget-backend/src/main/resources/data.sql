-- ====================================================================
-- NutriBudget Seed Data (Du lieu mau khoi tao)
-- Password mac dinh cho 3 user la: 123456 (BCrypt hash)
-- ====================================================================

-- 1. NGUOI DUNG (3 nguoi dung mau)
INSERT IGNORE INTO nguoi_dung (id, ho_ten, email, mat_khau, so_dien_thoai, gioi_tinh, ngay_sinh, chieu_cao, can_nang) VALUES
(1, 'Pham Nguyen Lan Anh', 'lananh@nutribudget.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '0987654321', 'Nu', '2002-05-15', 160.0, 48.5),
(2, 'Nguyen Van A', 'nguyenvana@gmail.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '0912345678', 'Nam', '2000-01-20', 172.0, 68.0),
(3, 'Tran Thi B', 'tranthib@gmail.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '0934567890', 'Nu', '1999-10-10', 158.0, 52.0);

-- 2. MUC TIEU DINH DUONG
INSERT IGNORE INTO muc_tieu_dinh_duong (id, nguoi_dung_id, muc_tieu, calo_muc_tieu, protein_muc_tieu, carb_muc_tieu, fat_muc_tieu) VALUES
(1, 1, 'Giam mo giu co', 1600.0, 75.0, 180.0, 40.0),
(2, 2, 'Tang co giam mo', 2500.0, 130.0, 300.0, 65.0),
(3, 3, 'Duy tri voc dang va suc khoe', 1800.0, 65.0, 220.0, 50.0);

-- 3. NGAN SACH
INSERT IGNORE INTO ngan_sach (id, nguoi_dung_id, ngan_sach_ngay, ngan_sach_tuan, ngan_sach_thang) VALUES
(1, 1, 80000.00, 550000.00, 2400000.00),
(2, 2, 120000.00, 800000.00, 3500000.00),
(3, 3, 70000.00, 480000.00, 2000000.00);

-- 4. MON AN (15 mon an Viet Nam truyen thong)
INSERT IGNORE INTO mon_an (id, ten_mon, mo_ta, hinh_anh, calo, protein, carb, fat, gia_du_kien, khau_phan) VALUES
(1, 'Pho bo', 'Pho bo truyen thong Ha Noi voi banh pho, bo tai chin va nuoc dung ninh xuong dam da', NULL, 450.0, 25.0, 55.0, 12.0, 35000.00, '1 to'),
(2, 'Banh mi trung op la', 'Banh mi gion nong voi trung ga op la, do chua, ngo va sot tuong ot', NULL, 380.0, 15.0, 42.0, 16.0, 20000.00, '1 o'),
(3, 'Bun rieu cua', 'Bun rieu cua dong thom ngon voi ca chua, dau hu ran vang va rau song', NULL, 420.0, 22.0, 50.0, 14.0, 30000.00, '1 to'),
(4, 'Xoi xeo ga xe', 'Xoi nep cai hoa vang deo thom an kem thit ga xe phay va hanh phi', NULL, 460.0, 20.0, 65.0, 14.0, 25000.00, '1 phan'),
(5, 'Com tam suon nuong', 'Com tam suon cot let nuong mat ong, cha trung hap, bi thom ngon', NULL, 620.0, 32.0, 70.0, 22.0, 35000.00, '1 dia'),
(6, 'Bun bo Hue', 'Bun bo Hue nuoc dung cay nhe dam vi sa ruoc kem bap bo va cha cua', NULL, 480.0, 28.0, 55.0, 16.0, 35000.00, '1 to'),
(7, 'Bun cha Ha Noi', 'Bun cha than nuong tren than hoa thom lung cham nuoc mam chua ngot', NULL, 510.0, 26.0, 58.0, 18.0, 35000.00, '1 phan'),
(8, 'Mi Quang ga', 'Mi Quang soi day voi thit ga om dam vi, dau phong rang va banh trang me', NULL, 440.0, 24.0, 52.0, 14.0, 30000.00, '1 to'),
(9, 'Com ga xoi mo', 'Com rang gion xoi mo vang ruom an cung dui ga chien gion sot toi ot', NULL, 680.0, 35.0, 60.0, 28.0, 40000.00, '1 dia'),
(10, 'Canh chua ca loc com trang', 'Canh chua ca loc nau thom bac ha dau bap kem bat com trang deo', NULL, 400.0, 25.0, 50.0, 10.0, 30000.00, '1 phan'),
(11, 'Thit kho tau voi trung com trang', 'Thit ba chi kho nuoc dua mem thom voi trung vit an kem com nong', NULL, 650.0, 30.0, 60.0, 28.0, 35000.00, '1 phan'),
(12, 'Rau muong xao toi com trang', 'Rau muong xanh gion xao toi thom nuc mui an kem com trang', NULL, 280.0, 6.0, 45.0, 8.0, 15000.00, '1 phan'),
(13, 'Goi cuon tom thit', 'Goi cuon banh trang thanh mat voi tom tuoi, thit ba chi, bun va rau thom', NULL, 280.0, 18.0, 30.0, 6.0, 25000.00, '4 cuon'),
(14, 'Bo luc lac khoai tay', 'Thit bo uc mem xao hanh tay ot chuong an kem khoai tay chien gion', NULL, 550.0, 32.0, 40.0, 24.0, 50000.00, '1 phan'),
(15, 'Sua chua nep cam', 'Mon trang mieng bo duong voi sua chua len men tu nhien va nep cam deo', NULL, 200.0, 5.0, 30.0, 6.0, 12000.00, '1 hu');

-- 5. NGUYEN LIEU (15 nguyen lieu thong dung)
INSERT IGNORE INTO nguyen_lieu (id, ten_nguyen_lieu, don_vi, gia, calo, protein, carb, fat) VALUES
(1, 'Thit bo bap', '100g', 28000.00, 250.0, 26.0, 0.0, 15.0),
(2, 'Thit ba chi heo', '100g', 18000.00, 290.0, 16.0, 0.0, 24.0),
(3, 'Uc ga phi le', '100g', 14000.00, 165.0, 31.0, 0.0, 3.6),
(4, 'Tom tuoi', '100g', 25000.00, 99.0, 24.0, 0.2, 0.3),
(5, 'Trung ga', '1 qua', 3500.00, 74.0, 6.3, 0.4, 5.0),
(6, 'Gao thom', '1 kg', 22000.00, 1300.0, 27.0, 280.0, 3.0),
(7, 'Bun tuoi', '500g', 10000.00, 550.0, 8.5, 125.0, 0.5),
(8, 'Banh pho tuoi', '500g', 12000.00, 600.0, 10.0, 135.0, 0.8),
(9, 'Banh mi o', '1 o', 5000.00, 230.0, 8.0, 45.0, 2.0),
(10, 'Rau muong', '1 bo', 8000.00, 40.0, 3.0, 6.0, 0.5),
(11, 'Ca chua', '500g', 15000.00, 90.0, 4.5, 20.0, 1.0),
(12, 'Dau hu trang', '2 mieng', 8000.00, 150.0, 16.0, 4.0, 9.0),
(13, 'Dau an', '1 chai 1L', 45000.00, 8840.0, 0.0, 0.0, 1000.0),
(14, 'Nuoc mam', '1 chai 500ml', 25000.00, 150.0, 20.0, 10.0, 0.0),
(15, 'Sua chua khong duong', '1 hop', 7000.00, 80.0, 4.0, 6.0, 3.0);

-- 6. THUC DON (2 thuc don mau)
INSERT IGNORE INTO thuc_don (id, nguoi_dung_id, ten_thuc_don, ngay_ap_dung, tong_calo, tong_protein, tong_carb, tong_fat, tong_chi_phi, trang_thai) VALUES
(1, 1, 'Thuc don giam can ngay 1', '2026-09-26', 1260.0, 63.0, 152.0, 40.0, 87000.00, 'HOAT_DONG'),
(2, 2, 'Thuc don tang co ngay 1', '2026-09-26', 2210.0, 115.0, 245.0, 78.0, 135000.00, 'HOAT_DONG');

-- 7. CHI TIET THUC DON
INSERT IGNORE INTO chi_tiet_thuc_don (id, thuc_don_id, mon_an_id, bua_an, so_luong, calo, chi_phi) VALUES
-- Thuc don 1 (Lan Anh)
(1, 1, 2, 'SANG', 1.0, 380.0, 20000.00),
(2, 1, 10, 'TRUA', 1.0, 400.0, 30000.00),
(3, 1, 13, 'TOI', 1.0, 280.0, 25000.00),
(4, 1, 15, 'PHU', 1.0, 200.0, 12000.00),
-- Thuc don 2 (Van A)
(5, 2, 1, 'SANG', 1.0, 450.0, 35000.00),
(6, 2, 5, 'TRUA', 1.0, 620.0, 35000.00),
(7, 2, 9, 'TOI', 1.0, 680.0, 40000.00),
(8, 2, 4, 'PHU', 1.0, 460.0, 25000.00);

-- 8. LICH SU THUC DON
INSERT IGNORE INTO lich_su_thuc_don (id, nguoi_dung_id, thuc_don_id, hanh_dong, thoi_gian, ghi_chu) VALUES
(1, 1, 1, 'TAO_MOI', '2026-09-26 08:00:00', 'He thong tao thuc don giam can ngay 1'),
(2, 2, 2, 'TAO_MOI', '2026-09-26 09:15:00', 'He thong tao thuc don tang co ngay 1');