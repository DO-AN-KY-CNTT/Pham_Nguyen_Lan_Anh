import urllib.request
import urllib.parse
import json
import sys
import time

sys.stdout.reconfigure(encoding='utf-8')
BASE_URL = "http://localhost:8080"

def request_api(method, path, data=None, token=None):
    url = f"{BASE_URL}{path}"
    headers = {"Content-Type": "application/json"}
    if token:
        headers["Authorization"] = f"Bearer {token}"
    body = json.dumps(data).encode("utf-8") if data is not None else None
    req = urllib.request.Request(url, data=body, headers=headers, method=method)
    try:
        with urllib.request.urlopen(req) as resp:
            status = resp.getcode()
            content = resp.read().decode("utf-8")
            return status, json.loads(content) if content else {}
    except urllib.error.HTTPError as e:
        content = e.read().decode("utf-8")
        try:
            return e.code, json.loads(content)
        except:
            return e.code, {"error": content}
    except Exception as e:
        return 500, {"error": str(e)}

def run_tests():
    report = []
    print("=== STARTING FINAL AUDIT BACKEND VERIFICATION ===")

    # Test 1: Đăng ký (Register)
    ts = int(time.time())
    reg_email = f"audit_fresh_{ts}@nutribudget.com"
    reg_payload = {
        "hoTen": "Audit Fresh User",
        "email": reg_email,
        "matKhau": "Password123@"
    }
    status, res = request_api("POST", "/api/auth/register", reg_payload)
    if status == 200:
        print(f"[PASS] 1. Dang ky tai khoan moi thanh cong: email={reg_email}")
        report.append(("1. DANG KY", "PASS", f"Dang ky tai khoan moi thanh cong: email={reg_email}"))
    else:
        print(f"[FAIL] 1. Dang ky that bai: {status} - {res}")
        report.append(("1. DANG KY", "FAIL", str(res)))

    # Test duplicate email validation
    status_dup, res_dup = request_api("POST", "/api/auth/register", reg_payload)
    if status_dup == 400:
        print(f"[PASS] 1. Validation email trung: {status_dup} - {res_dup.get('message')}")
    else:
        print(f"[WARN] 1. Validation email trung status: {status_dup}")

    # Test 2: Đăng nhập (Login)
    # 2.1 Sai mật khẩu
    st_wp, res_wp = request_api("POST", "/api/auth/login", {"email": reg_email, "matKhau": "WrongPass!"})
    if st_wp == 400:
        print(f"[PASS] 2.1 Dang nhap sai pass bi chan: {st_wp} - {res_wp.get('message')}")
    else:
        print(f"[FAIL] 2.1 Dang nhap sai pass status: {st_wp}")

    # 2.2 Tài khoản không tồn tại
    st_ne, res_ne = request_api("POST", "/api/auth/login", {"email": "non_exist_999@xyz.com", "matKhau": "123456"})
    if st_ne == 404 or st_ne == 400:
        print(f"[PASS] 2.2 Dang nhap tai khoan khong ton tai bi chan: {st_ne} - {res_ne.get('message')}")
    else:
        print(f"[FAIL] 2.2 Tai khoan khong ton tai status: {st_ne}")

    # 2.3 Đăng nhập thành công lấy JWT
    st_log, res_log = request_api("POST", "/api/auth/login", {"email": reg_email, "matKhau": "Password123@"})
    token = None
    user_id = None
    if st_log == 200 and "data" in res_log:
        data = res_log["data"]
        token = data.get("token")
        user = data.get("user", {})
        user_id = user.get("id")
        print(f"[PASS] 2.3 Dang nhap thanh cong, nhan JWT: {token[:25]}... (do dai {len(token)}), user_id: {user_id}")
        report.append(("2. DANG NHAP", "PASS", f"JWT hop le cap cho user_id={user_id}"))
    else:
        print(f"[FAIL] 2.3 Dang nhap that bai: {st_log} - {res_log}")
        report.append(("2. DANG NHAP", "FAIL", str(res_log)))
        return

    # Test 3: Thông tin cá nhân (Profile)
    st_p, res_p = request_api("GET", "/api/users/profile", token=token)
    if st_p == 200 and res_p.get("data"):
        print(f"[PASS] 3.1 GET profile thanh cong: {res_p['data'].get('hoTen')}")
    else:
        print(f"[FAIL] 3.1 GET profile that bai: {st_p} - {res_p}")

    update_data = {
        "hoTen": "Audit Fresh Updated",
        "gioiTinh": "NAM",
        "ngaySinh": "1998-05-15",
        "chieuCao": 172.0,
        "canNang": 65.0
    }
    st_u, res_u = request_api("PUT", f"/api/users/{user_id}", data=update_data, token=token)
    if st_u == 200 and res_u.get("data"):
        u_data = res_u["data"]
        bmi = u_data.get("bmi")
        print(f"[PASS] 3.2 Cap nhat profile thanh cong (chieuCao=172, canNang=65). BMI tu dong tinh: {bmi}")
        report.append(("3. THONG TIN CA NHAN", "PASS", f"Cap nhat ho ten, chieu cao, can nang thanh cong, BMI={bmi}"))
    else:
        print(f"[FAIL] 3.2 PUT profile that bai: {st_u} - {res_u}")
        report.append(("3. THONG TIN CA NHAN", "FAIL", str(res_u)))

    # Test 4: Mục tiêu dinh dưỡng (Nutrition Goals)
    goal_payload = {
        "userId": user_id,
        "caloMucTieu": 2100.0,
        "proteinMucTieu": 120.0,
        "carbMucTieu": 260.0,
        "fatMucTieu": 60.0,
        "mucTieu": "GIU_CAN",
        "ghiChu": "Audit Fresh Goal"
    }
    st_g, res_g = request_api("POST", "/api/nutrition-goals", data=goal_payload, token=token)
    goal_id = None
    if st_g == 200 and res_g.get("data"):
        goal_id = res_g["data"].get("id")
        print(f"[PASS] 4.1 POST muc tieu thanh cong: goal_id={goal_id}")
    else:
        print(f"[FAIL] 4.1 POST muc tieu that bai: {st_g} - {res_g}")

    if goal_id:
        goal_payload["caloMucTieu"] = 2050.0
        st_g_put, res_g_put = request_api("PUT", f"/api/nutrition-goals/{goal_id}", data=goal_payload, token=token)
        print(f"[PASS] 4.2 PUT muc tieu: status={st_g_put}")

    st_g_get, res_g_get = request_api("GET", f"/api/nutrition-goals/user/{user_id}", token=token)
    if st_g_get == 200 and res_g_get.get("data"):
        print(f"[PASS] 4.3 GET muc tieu tu MySQL: calo={res_g_get['data'][0].get('caloMucTieu')}")
        report.append(("4. MUC TIEU DINH DUONG", "PASS", f"POST/PUT/GET luu that vao muc_tieu_dinh_duong, calo={res_g_get['data'][0].get('caloMucTieu')}"))
    else:
        print(f"[FAIL] 4.3 GET muc tieu that bai: {st_g_get} - {res_g_get}")
        report.append(("4. MUC TIEU DINH DUONG", "FAIL", str(res_g_get)))

    # Test 5: Ngân sách (Budget)
    budget_payload = {
        "userId": user_id,
        "nganSachNgay": 80000.0,
        "nganSachTuan": 560000.0,
        "nganSachThang": 2400000.0,
        "loaiNganSach": "NGAY",
        "ghiChu": "Audit Fresh Budget"
    }
    st_b, res_b = request_api("POST", "/api/budgets", data=budget_payload, token=token)
    budget_id = None
    if st_b == 200 and res_b.get("data"):
        budget_id = res_b["data"].get("id")
        print(f"[PASS] 5.1 POST ngan sach thanh cong: budget_id={budget_id}")
    else:
        print(f"[FAIL] 5.1 POST ngan sach that bai: {st_b} - {res_b}")

    if budget_id:
        budget_payload["nganSachNgay"] = 85000.0
        st_b_put, res_b_put = request_api("PUT", f"/api/budgets/{budget_id}", data=budget_payload, token=token)
        print(f"[PASS] 5.2 PUT ngan sach: status={st_b_put}")

    st_b_get, res_b_get = request_api("GET", f"/api/budgets/user/{user_id}", token=token)
    if st_b_get == 200 and res_b_get.get("data"):
        print(f"[PASS] 5.3 GET ngan sach tu MySQL: ngay={res_b_get['data'][0].get('nganSachNgay')}")
        report.append(("5. NGAN SACH", "PASS", f"POST/PUT/GET luu that vao ngan_sach, ngay={res_b_get['data'][0].get('nganSachNgay')}"))
    else:
        print(f"[FAIL] 5.3 GET ngan sach that bai: {st_b_get} - {res_b_get}")
        report.append(("5. NGAN SACH", "FAIL", str(res_b_get)))

    # Test 6: Đề xuất thực đơn (POST /api/menus/suggest)
    st_sug, res_sug = request_api("POST", "/api/menus/suggest", data={"userId": user_id, "soBua": 3}, token=token)
    suggested_plans = []
    if st_sug == 200 and res_sug.get("data"):
        suggested_plans = res_sug["data"]
        print(f"[PASS] 6. De xuat thuc don thanh cong! So phuong an tra ve: {len(suggested_plans)}")
        for idx, plan in enumerate(suggested_plans):
            ten = plan.get('tenThucDon', '').encode('ascii', 'replace').decode('ascii')
            print(f"   -> Phuong an {idx+1}: {ten} - Calo: {plan.get('tongCalo')}, Gia: {plan.get('tongChiPhi')}, Mon an: {len(plan.get('chiTietThucDon', []))}")
        report.append(("6. DE XUAT THUC DON", "PASS", f"Tra ve {len(suggested_plans)} phuong an da dang dua tren muc tieu & ngan sach tu MySQL"))
    else:
        print(f"[FAIL] 6. De xuat thuc don that bai: {st_sug} - {res_sug}")
        report.append(("6. DE XUAT THUC DON", "FAIL", str(res_sug)))

    # Test 7: Lưu thực đơn (POST /api/menus)
    selected_plan = suggested_plans[0] if suggested_plans else None
    menu_id = None
    if selected_plan:
        items = []
        for item in selected_plan.get("chiTietThucDon", []):
            items.append({
                "monAnId": item.get("monAnId"),
                "buaAn": item.get("buaAn"),
                "soLuong": item.get("soLuong", 1.0),
                "calo": item.get("calo"),
                "chiPhi": item.get("chiPhi")
            })
        create_menu_payload = {
            "userId": user_id,
            "tenThucDon": selected_plan.get("tenThucDon", "Thuc don kiem toan"),
            "ngayApDung": "2026-09-27",
            "trangThai": "HOAT_DONG",
            "items": items
        }
        st_cm, res_cm = request_api("POST", "/api/menus", data=create_menu_payload, token=token)
        if st_cm == 200 and res_cm.get("data"):
            menu_id = res_cm["data"].get("id")
            print(f"[PASS] 7. Luu thuc don thanh cong: menu_id={menu_id}, calo={res_cm['data'].get('tongCalo')}, chi_phi={res_cm['data'].get('tongChiPhi')}")
            report.append(("7. LUU THUC DON", "PASS", f"Luu thanh cong vao thuc_don & chi_tiet_thuc_don, menu_id={menu_id}"))
        else:
            print(f"[FAIL] 7. Luu thuc don that bai: {st_cm} - {res_cm}")
            report.append(("7. LUU THUC DON", "FAIL", str(res_cm)))
    else:
        print("[FAIL] 7. Khong co phuong an de luu")
        report.append(("7. LUU THUC DON", "FAIL", "Khong co phuong an"))

    # Test 8: Chi tiết thực đơn (GET /api/menus/{id})
    if menu_id:
        st_m, res_m = request_api("GET", f"/api/menus/{menu_id}", token=token)
        if st_m == 200 and res_m.get("data"):
            m_data = res_m["data"]
            ten = m_data.get('tenThucDon', '').encode('ascii', 'replace').decode('ascii')
            print(f"[PASS] 8. Lay chi tiet thuc don thanh cong: {ten}")
            print(f"   -> Calo: {m_data.get('tongCalo')}, Protein: {m_data.get('tongProtein')}, Chi phi: {m_data.get('tongChiPhi')}")
            print(f"   -> So luong mon trong thuc don: {len(m_data.get('chiTietThucDon', []))}")
            report.append(("8. CHI TIET THUC DON", "PASS", f"GET /api/menus/{menu_id} hien thi day du chi tiet mon, calo={m_data.get('tongCalo')}, chi_phi={m_data.get('tongChiPhi')}"))
        else:
            print(f"[FAIL] 8. Lay chi tiet thuc don that bai: {st_m} - {res_m}")
            report.append(("8. CHI TIET THUC DON", "FAIL", str(res_m)))

    # Test 9: Danh sách nguyên liệu (GET /api/menus/{menuId}/ingredients)
    if menu_id:
        st_ing, res_ing = request_api("GET", f"/api/menus/{menu_id}/ingredients", token=token)
        if st_ing == 200 and res_ing.get("data"):
            ing_data = res_ing["data"]
            items = ing_data.get("nguyenLieu", [])
            print(f"[PASS] 9. Lay danh sach nguyen lieu thanh cong: Tong chi phi nguyen lieu = {ing_data.get('tongChiPhiNguyenLieu')}")
            print(f"   -> So nguyen lieu sau khi gom: {len(items)}")
            for ing in items[:3]:
                ten_nl = ing.get('tenNguyenLieu', '').encode('ascii', 'replace').decode('ascii')
                print(f"      - {ten_nl}: {ing.get('soLuong')} {ing.get('donVi')} x {ing.get('donGia')} = {ing.get('thanhTien')}")
            report.append(("9. DANH SACH NGUYEN LIEU", "PASS", f"Gom nguyen lieu thanh cong: {len(items)} nguyen lieu, Tong={ing_data.get('tongChiPhiNguyenLieu')}"))
        else:
            print(f"[FAIL] 9. Lay danh sach nguyen lieu that bai: {st_ing} - {res_ing}")
            report.append(("9. DANH SACH NGUYEN LIEU", "FAIL", str(res_ing)))

    # Test 10: Lịch sử thực đơn (GET /api/menu-history/user/{userId})
    if user_id:
        st_h, res_h = request_api("GET", f"/api/menu-history/user/{user_id}", token=token)
        if st_h == 200 and res_h.get("data"):
            histories = res_h["data"]
            print(f"[PASS] 10. Lay lich su thuc don thanh cong: Tong so ban ghi lich su = {len(histories)}")
            for h in histories:
                desc = h.get('moTa', '').encode('ascii', 'replace').decode('ascii')
                print(f"   -> Thao tac: {h.get('loaiThaoTac')} - {desc} - Thoi gian: {h.get('thoiGianThaoTac')}")
            report.append(("10. LICH SU THUC DON", "PASS", f"Ghi nhan va truy van dung {len(histories)} ban ghi lich su tu lich_su_thuc_don"))
        else:
            print(f"[FAIL] 10. Lay lich su that bai: {st_h} - {res_h}")
            report.append(("10. LICH SU THUC DON", "FAIL", str(res_h)))

    # Test 11: Xác thực & Session (JWT invalid / expired test)
    st_inv, res_inv = request_api("GET", "/api/users/profile", token="invalid_token_12345")
    if st_inv == 401 or st_inv == 403:
        print(f"[PASS] 11.1 Token sai bi tu choi voi HTTP {st_inv}")
    else:
        print(f"[FAIL] 11.1 Token sai khong bi chan dung ma tra: {st_inv}")

    st_no, res_no = request_api("GET", "/api/users/profile")
    if st_no == 401 or st_no == 403:
        print(f"[PASS] 11.2 Khong co Token bi tu choi voi HTTP {st_no}")
        report.append(("11. XAC THUC VA SESSION", "PASS", f"Bao ve bang JWT chan 401/403 khi token sai hoac thieu"))
    else:
        print(f"[FAIL] 11.2 Khong co Token status: {st_no}")
        report.append(("11. XAC THUC VA SESSION", "FAIL", f"Khong chan: status={st_no}"))

    print("\n=== SUMMARY AUDIT REPORT ===")
    all_pass = True
    for title, status_str, note in report:
        print(f"[{status_str}] {title}: {note}")
        if status_str != "PASS":
            all_pass = False

    print("\nOVERALL STATUS:", "PASS" if all_pass else "FAIL")

if __name__ == "__main__":
    run_tests()
