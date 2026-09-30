-- ================================================================
-- SCRIPT FIX: Phan bo lai bua an cho chi_tiet_thuc_don bi sai
-- Nguyen nhan: Android luu tat ca la TRUA do loi parse tieng Viet
-- 
-- Quy tac phan bo tu dong:
--   Mon 1 (index nho nhat) => SANG
--   Mon 2 (index giua)     => TRUA  
--   Mon 3 (index lon nhat) => TOI
--   Mon 4+ (neu co)        => PHU
--
-- Chay script nay trong MySQL Workbench hoac CLI
-- ================================================================

-- Xem truoc du lieu bi loi (tat ca la TRUA trong cung 1 thuc don)
SELECT 
    td.id AS menu_id,
    td.ten_thuc_don,
    ct.id AS detail_id,
    ct.mon_an_id,
    ma.ten_mon,
    ct.bua_an
FROM thuc_don td
JOIN chi_tiet_thuc_don ct ON ct.thuc_don_id = td.id
JOIN mon_an ma ON ma.id = ct.mon_an_id
WHERE td.id IN (
    SELECT thuc_don_id 
    FROM chi_tiet_thuc_don 
    GROUP BY thuc_don_id 
    HAVING COUNT(*) >= 2 
       AND COUNT(DISTINCT bua_an) = 1 
       AND MAX(bua_an) = 'TRUA'
)
ORDER BY td.id, ct.id;

-- ================================================================
-- PHAN BO LAI BUA AN DUA TREN THU TU (sort by detail ID)
-- ================================================================

-- Tao bang tam: danh so thu tu moi mon trong moi thuc don
CREATE TEMPORARY TABLE IF NOT EXISTS temp_meal_rank AS
SELECT 
    ct.id AS detail_id,
    ct.thuc_don_id,
    ct.bua_an AS bua_an_cu,
    ROW_NUMBER() OVER (PARTITION BY ct.thuc_don_id ORDER BY ct.id ASC) AS stt,
    COUNT(*) OVER (PARTITION BY ct.thuc_don_id) AS total_items
FROM chi_tiet_thuc_don ct
WHERE ct.thuc_don_id IN (
    -- Chi sua nhung thuc don bi sai (tat ca cung = TRUA)
    SELECT thuc_don_id 
    FROM chi_tiet_thuc_don 
    GROUP BY thuc_don_id 
    HAVING COUNT(*) >= 2 
       AND COUNT(DISTINCT bua_an) = 1 
       AND MAX(bua_an) = 'TRUA'
);

-- Xem thu tu truoc khi sua
SELECT 
    detail_id, thuc_don_id, bua_an_cu, stt, total_items,
    CASE stt 
        WHEN 1 THEN 'SANG'
        WHEN 2 THEN CASE WHEN total_items = 2 THEN 'TOI' ELSE 'TRUA' END
        WHEN 3 THEN CASE WHEN total_items = 3 THEN 'TOI' ELSE 'TOI' END
        ELSE 'PHU'
    END AS bua_an_moi
FROM temp_meal_rank
ORDER BY thuc_don_id, stt;

-- ================================================================
-- THUC HIEN CAP NHAT
-- ================================================================

UPDATE chi_tiet_thuc_don ct
JOIN temp_meal_rank r ON r.detail_id = ct.id
SET ct.bua_an = 
    CASE r.stt
        WHEN 1 THEN 'SANG'
        WHEN 2 THEN 
            CASE WHEN r.total_items = 2 THEN 'TOI'
                 ELSE 'TRUA'
            END
        WHEN 3 THEN 'TOI'
        ELSE 'PHU'
    END;

-- Don dep
DROP TEMPORARY TABLE IF EXISTS temp_meal_rank;

-- Kiem tra ket qua sau khi sua
SELECT 
    td.id AS menu_id,
    td.ten_thuc_don,
    ct.id AS detail_id,
    ma.ten_mon,
    ct.bua_an AS bua_an_sau_fix
FROM thuc_don td
JOIN chi_tiet_thuc_don ct ON ct.thuc_don_id = td.id
JOIN mon_an ma ON ma.id = ct.mon_an_id
WHERE td.id IN (9, 10)
ORDER BY td.id, ct.id;

-- ================================================================
-- KIEM TRA LAI TONG CHI PHI THEO BUA (goi API sau khi chay script)
-- GET /api/menus/9/cost  va  GET /api/menus/10/cost
-- Ket qua mong doi: chiPhiTheoBua co ca SANG, TRUA, TOI khac 0
-- ================================================================
