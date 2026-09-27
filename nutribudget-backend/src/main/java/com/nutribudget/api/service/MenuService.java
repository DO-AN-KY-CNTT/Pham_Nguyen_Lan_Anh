package com.nutribudget.api.service;

import com.nutribudget.api.dto.request.MenuDetailRequest;
import com.nutribudget.api.dto.request.MenuRequest;
import com.nutribudget.api.dto.request.SuggestMenuRequest;
import com.nutribudget.api.dto.request.UpdateMenuItemRequest;
import com.nutribudget.api.dto.response.MenuDetailResponse;
import com.nutribudget.api.dto.response.MenuResponse;
import com.nutribudget.api.dto.response.MenuCostResponse;
import com.nutribudget.api.dto.response.SuggestedMenuResponse;
import com.nutribudget.api.entity.Budget;
import com.nutribudget.api.entity.Dish;
import com.nutribudget.api.entity.Menu;
import com.nutribudget.api.entity.MenuDetail;
import com.nutribudget.api.entity.NutritionGoal;
import com.nutribudget.api.entity.User;
import com.nutribudget.api.exception.ResourceNotFoundException;
import com.nutribudget.api.repository.BudgetRepository;
import com.nutribudget.api.repository.DishRepository;
import com.nutribudget.api.repository.MenuRepository;
import com.nutribudget.api.repository.NutritionGoalRepository;
import com.nutribudget.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nutribudget.api.dto.response.IngredientItemResponse;
import com.nutribudget.api.dto.response.MenuIngredientsResponse;
import com.nutribudget.api.entity.DishIngredient;
import com.nutribudget.api.entity.Ingredient;
import com.nutribudget.api.repository.DishIngredientRepository;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private MenuHistoryService historyService;

    @Autowired
    private NutritionGoalRepository nutritionGoalRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private DishIngredientRepository dishIngredientRepository;

    public List<MenuResponse> getMenusByUserId(Long userId) {
        return menuRepository.findByNguoiDungIdOrderByNgayApDungDesc(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MenuResponse getMenuById(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay thuc don voi ID: " + id));
        return toResponse(menu);
    }

    @Transactional
    public MenuResponse createMenu(MenuRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung voi ID: " + request.getUserId()));

        Menu menu = Menu.builder()
                .nguoiDung(user)
                .tenThucDon(request.getTenThucDon())
                .ngayApDung(request.getNgayApDung())
                .trangThai(request.getTrangThai() != null ? request.getTrangThai() : "HOAT_DONG")
                .chiTietThucDon(new ArrayList<>())
                .build();

        double totalCalo = 0.0;
        double totalProtein = 0.0;
        double totalCarb = 0.0;
        double totalFat = 0.0;
        BigDecimal totalCost = BigDecimal.ZERO;

        if (request.getItems() != null) {
            for (MenuDetailRequest itemReq : request.getItems()) {
                Dish dish = dishRepository.findById(itemReq.getMonAnId())
                        .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay mon an voi ID: " + itemReq.getMonAnId()));

                double qty = itemReq.getSoLuong() != null ? itemReq.getSoLuong() : 1.0;
                double itemCalo = (dish.getCalo() != null ? dish.getCalo() : 0.0) * qty;
                BigDecimal itemCost = dish.getGiaDuKien() != null
                        ? dish.getGiaDuKien().multiply(BigDecimal.valueOf(qty))
                        : BigDecimal.ZERO;

                MenuDetail detail = MenuDetail.builder()
                        .thucDon(menu)
                        .monAn(dish)
                        .buaAn(itemReq.getBuaAn())
                        .soLuong(qty)
                        .calo(itemReq.getCalo() != null ? itemReq.getCalo() : itemCalo)
                        .chiPhi(itemReq.getChiPhi() != null ? itemReq.getChiPhi() : itemCost)
                        .build();

                menu.getChiTietThucDon().add(detail);

                totalCalo += detail.getCalo() != null ? detail.getCalo() : 0.0;
                totalProtein += (dish.getProtein() != null ? dish.getProtein() : 0.0) * qty;
                totalCarb += (dish.getCarb() != null ? dish.getCarb() : 0.0) * qty;
                totalFat += (dish.getFat() != null ? dish.getFat() : 0.0) * qty;
                if (detail.getChiPhi() != null) {
                    totalCost = totalCost.add(detail.getChiPhi());
                }
            }
        }

        menu.setTongCalo(totalCalo);
        menu.setTongProtein(totalProtein);
        menu.setTongCarb(totalCarb);
        menu.setTongFat(totalFat);
        menu.setTongChiPhi(totalCost);

        menu = menuRepository.save(menu);

        // Record history
        historyService.recordHistory(user, menu, "TAO_MOI", "Tao thuc don: " + menu.getTenThucDon());

        return toResponse(menu);
    }

    @Transactional
    public MenuResponse updateMenu(Long id, MenuRequest request) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay thuc don voi ID: " + id));

        if (request.getTenThucDon() != null) menu.setTenThucDon(request.getTenThucDon());
        if (request.getNgayApDung() != null) menu.setNgayApDung(request.getNgayApDung());
        if (request.getTrangThai() != null) menu.setTrangThai(request.getTrangThai());

        if (request.getItems() != null) {
            menu.getChiTietThucDon().clear();
            double totalCalo = 0.0;
            double totalProtein = 0.0;
            double totalCarb = 0.0;
            double totalFat = 0.0;
            BigDecimal totalCost = BigDecimal.ZERO;

            for (MenuDetailRequest itemReq : request.getItems()) {
                Dish dish = dishRepository.findById(itemReq.getMonAnId())
                        .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay mon an voi ID: " + itemReq.getMonAnId()));

                double qty = itemReq.getSoLuong() != null ? itemReq.getSoLuong() : 1.0;
                double itemCalo = (dish.getCalo() != null ? dish.getCalo() : 0.0) * qty;
                BigDecimal itemCost = dish.getGiaDuKien() != null
                        ? dish.getGiaDuKien().multiply(BigDecimal.valueOf(qty))
                        : BigDecimal.ZERO;

                MenuDetail detail = MenuDetail.builder()
                        .thucDon(menu)
                        .monAn(dish)
                        .buaAn(itemReq.getBuaAn())
                        .soLuong(qty)
                        .calo(itemReq.getCalo() != null ? itemReq.getCalo() : itemCalo)
                        .chiPhi(itemReq.getChiPhi() != null ? itemReq.getChiPhi() : itemCost)
                        .build();

                menu.getChiTietThucDon().add(detail);

                totalCalo += detail.getCalo() != null ? detail.getCalo() : 0.0;
                totalProtein += (dish.getProtein() != null ? dish.getProtein() : 0.0) * qty;
                totalCarb += (dish.getCarb() != null ? dish.getCarb() : 0.0) * qty;
                totalFat += (dish.getFat() != null ? dish.getFat() : 0.0) * qty;
                if (detail.getChiPhi() != null) {
                    totalCost = totalCost.add(detail.getChiPhi());
                }
            }

            menu.setTongCalo(totalCalo);
            menu.setTongProtein(totalProtein);
            menu.setTongCarb(totalCarb);
            menu.setTongFat(totalFat);
            menu.setTongChiPhi(totalCost);
        }

        menu = menuRepository.save(menu);

        // Record history
        historyService.recordHistory(menu.getNguoiDung(), menu, "CAP_NHAT", "Cap nhat thuc don: " + menu.getTenThucDon());

        return toResponse(menu);
    }

    @Transactional
    public MenuResponse updateMenuItem(Long menuId, Long itemId, UpdateMenuItemRequest request) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay thuc don voi ID: " + menuId));

        Dish dish = dishRepository.findById(request.getMonAnId())
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay mon an voi ID: " + request.getMonAnId()));

        MenuDetail targetDetail = null;
        if (menu.getChiTietThucDon() != null) {
            for (MenuDetail detail : menu.getChiTietThucDon()) {
                if (detail.getId() != null && detail.getId().equals(itemId)) {
                    targetDetail = detail;
                    break;
                }
            }
            if (targetDetail == null && request.getBuaAn() != null) {
                for (MenuDetail detail : menu.getChiTietThucDon()) {
                    if (request.getBuaAn().equalsIgnoreCase(detail.getBuaAn())) {
                        targetDetail = detail;
                        break;
                    }
                }
            }
        }

        if (targetDetail == null) {
            throw new ResourceNotFoundException("Khong tim thay mon an trong thuc don voi ID chi tiet: " + itemId);
        }

        double qty = (request.getSoLuong() != null && request.getSoLuong() > 0) ? request.getSoLuong() : 1.0;
        targetDetail.setMonAn(dish);
        if (request.getBuaAn() != null && !request.getBuaAn().trim().isEmpty()) {
            targetDetail.setBuaAn(request.getBuaAn());
        }
        targetDetail.setSoLuong(qty);
        double itemCalo = (dish.getCalo() != null ? dish.getCalo() : 0.0) * qty;
        BigDecimal itemCost = dish.getGiaDuKien() != null
                ? dish.getGiaDuKien().multiply(BigDecimal.valueOf(qty))
                : BigDecimal.ZERO;
        targetDetail.setCalo(itemCalo);
        targetDetail.setChiPhi(itemCost);

        // Tinh lai toan bo tong calo, protein, carb, fat va tong chi phi
        double totalCalo = 0.0;
        double totalProtein = 0.0;
        double totalCarb = 0.0;
        double totalFat = 0.0;
        BigDecimal totalCost = BigDecimal.ZERO;

        for (MenuDetail d : menu.getChiTietThucDon()) {
            Dish itemDish = d.getMonAn();
            double itemQty = d.getSoLuong() != null ? d.getSoLuong() : 1.0;

            totalCalo += d.getCalo() != null ? d.getCalo() : 0.0;
            totalProtein += (itemDish.getProtein() != null ? itemDish.getProtein() : 0.0) * itemQty;
            totalCarb += (itemDish.getCarb() != null ? itemDish.getCarb() : 0.0) * itemQty;
            totalFat += (itemDish.getFat() != null ? itemDish.getFat() : 0.0) * itemQty;
            if (d.getChiPhi() != null) {
                totalCost = totalCost.add(d.getChiPhi());
            }
        }

        menu.setTongCalo(Math.round(totalCalo * 10.0) / 10.0);
        menu.setTongProtein(Math.round(totalProtein * 10.0) / 10.0);
        menu.setTongCarb(Math.round(totalCarb * 10.0) / 10.0);
        menu.setTongFat(Math.round(totalFat * 10.0) / 10.0);
        menu.setTongChiPhi(totalCost);

        menu = menuRepository.save(menu);

        // Ghi lich su thao tac voi hanh dong DOI_MON
        historyService.recordHistory(
                menu.getNguoiDung(),
                menu,
                "DOI_MON",
                "Doi mon an sang: " + dish.getTenMon() + " (" + targetDetail.getBuaAn() + ")"
        );

        return toResponse(menu);
    }


    public List<SuggestedMenuResponse> suggestMenus(SuggestMenuRequest request, String userEmail) {
        // 1. Resolve user
        User user = null;
        if (request != null && request.getUserId() != null) {
            user = userRepository.findById(request.getUserId()).orElse(null);
        }
        if (user == null && userEmail != null) {
            user = userRepository.findByEmail(userEmail).orElse(null);
        }
        if (user == null) {
            user = userRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung"));
        }

        // 2. Fetch User Goal from Database
        double targetCalo = 2000.0;
        double targetProtein = 100.0;
        double targetCarb = 250.0;
        double targetFat = 60.0;

        List<NutritionGoal> goals = nutritionGoalRepository.findByNguoiDungId(user.getId());
        if (!goals.isEmpty()) {
            NutritionGoal g = goals.get(0);
            if (g.getCaloMucTieu() != null) targetCalo = g.getCaloMucTieu();
            if (g.getProteinMucTieu() != null) targetProtein = g.getProteinMucTieu();
            if (g.getCarbMucTieu() != null) targetCarb = g.getCarbMucTieu();
            if (g.getFatMucTieu() != null) targetFat = g.getFatMucTieu();
        }

        // 3. Fetch User Budget from Database
        double dailyBudget = 100000.0;
        List<Budget> budgets = budgetRepository.findByNguoiDungId(user.getId());
        if (!budgets.isEmpty()) {
            Budget b = budgets.get(0);
            if (b.getNganSachNgay() != null) {
                dailyBudget = b.getNganSachNgay().doubleValue();
            }
        }

        // 4. Fetch Dishes from CSDL
        List<Dish> allDishes = dishRepository.findAll();
        if (allDishes.isEmpty()) {
            return Collections.emptyList();
        }

        // Categorize dishes by meal type
        List<Dish> sangDishes = new ArrayList<>();
        List<Dish> truaDishes = new ArrayList<>();
        List<Dish> toiDishes = new ArrayList<>();

        for (Dish d : allDishes) {
            long id = d.getId();
            if (id == 1 || id == 2 || id == 3 || id == 4 || id == 6 || id == 8) {
                sangDishes.add(d);
            }
            if (id == 5 || id == 7 || id == 9 || id == 10 || id == 11 || id == 14) {
                truaDishes.add(d);
            }
            if (id == 3 || id == 8 || id == 10 || id == 12 || id == 13 || id == 14) {
                toiDishes.add(d);
            }
        }

        if (sangDishes.isEmpty()) sangDishes = allDishes;
        if (truaDishes.isEmpty()) truaDishes = allDishes;
        if (toiDishes.isEmpty()) toiDishes = allDishes;

        LocalDate applyDate = (request != null && request.getNgayApDung() != null) ? request.getNgayApDung() : LocalDate.now();

        // 5. Generate Combinations and Score
        List<MenuCombo> combos = new ArrayList<>();
        for (Dish s : sangDishes) {
            for (Dish t : truaDishes) {
                for (Dish d : toiDishes) {
                    if (s.getId().equals(t.getId()) || t.getId().equals(d.getId())) {
                        continue;
                    }
                    MenuCombo c = new MenuCombo();
                    c.sang = s;
                    c.trua = t;
                    c.toi = d;

                    double cCalo = (s.getCalo() != null ? s.getCalo() : 0) +
                                  (t.getCalo() != null ? t.getCalo() : 0) +
                                  (d.getCalo() != null ? d.getCalo() : 0);
                    double cProtein = (s.getProtein() != null ? s.getProtein() : 0) +
                                     (t.getProtein() != null ? t.getProtein() : 0) +
                                     (d.getProtein() != null ? d.getProtein() : 0);
                    double cCarb = (s.getCarb() != null ? s.getCarb() : 0) +
                                  (t.getCarb() != null ? t.getCarb() : 0) +
                                  (d.getCarb() != null ? d.getCarb() : 0);
                    double cFat = (s.getFat() != null ? s.getFat() : 0) +
                                 (t.getFat() != null ? t.getFat() : 0) +
                                 (d.getFat() != null ? d.getFat() : 0);
                    double cCost = (s.getGiaDuKien() != null ? s.getGiaDuKien().doubleValue() : 0) +
                                  (t.getGiaDuKien() != null ? t.getGiaDuKien().doubleValue() : 0) +
                                  (d.getGiaDuKien() != null ? d.getGiaDuKien().doubleValue() : 0);

                    c.calo = cCalo;
                    c.protein = cProtein;
                    c.carb = cCarb;
                    c.fat = cFat;
                    c.cost = cCost;

                    // Calorie deviation
                    double diffCalo = Math.abs(cCalo - targetCalo) / targetCalo;
                    // Budget deviation
                    double diffCost;
                    if (cCost <= dailyBudget) {
                        diffCost = ((dailyBudget - cCost) / dailyBudget) * 0.15;
                    } else {
                        diffCost = ((cCost - dailyBudget) / dailyBudget) * 2.0;
                    }
                    double diffProtein = Math.abs(cProtein - targetProtein) / targetProtein;

                    c.balancedScore = 1.0 - (0.50 * diffCalo + 0.35 * diffCost + 0.15 * diffProtein);

                    if (cCost <= dailyBudget && diffCalo <= 0.35) {
                        c.savingsScore = (dailyBudget - cCost) - (diffCalo * 20000);
                    } else {
                        c.savingsScore = -999999;
                    }

                    if (cCost <= dailyBudget * 1.15) {
                        c.proteinScore = cProtein * 100 - (diffCalo * 5000);
                    } else {
                        c.proteinScore = -999999;
                    }

                    combos.add(c);
                }
            }
        }

        if (combos.isEmpty()) {
            return Collections.emptyList();
        }

        // 6. Select Top 3 Diverse Plans
        combos.sort((a, b) -> Double.compare(b.balancedScore, a.balancedScore));
        MenuCombo bestBalanced = combos.get(0);

        List<MenuCombo> bySavings = new ArrayList<>(combos);
        bySavings.sort((a, b) -> Double.compare(b.savingsScore, a.savingsScore));
        MenuCombo bestSavings = null;
        for (MenuCombo c : bySavings) {
            if (!isSameCombo(c, bestBalanced)) {
                bestSavings = c;
                break;
            }
        }
        if (bestSavings == null) bestSavings = combos.size() > 1 ? combos.get(1) : bestBalanced;

        List<MenuCombo> byProtein = new ArrayList<>(combos);
        byProtein.sort((a, b) -> Double.compare(b.proteinScore, a.proteinScore));
        MenuCombo bestProtein = null;
        for (MenuCombo c : byProtein) {
            if (!isSameCombo(c, bestBalanced) && !isSameCombo(c, bestSavings)) {
                bestProtein = c;
                break;
            }
        }
        if (bestProtein == null) bestProtein = combos.size() > 2 ? combos.get(2) : bestBalanced;

        List<SuggestedMenuResponse> result = new ArrayList<>();

        result.add(createSuggestedPlan("plan_1", "Thực đơn Cân đối Tối ưu",
                "Chuẩn calo & tối ưu dinh dưỡng hôm nay",
                bestBalanced, targetCalo, dailyBudget, applyDate));

        result.add(createSuggestedPlan("plan_2", "Thực đơn Tiết kiệm & Tối ưu Ngân sách",
                "Tiết kiệm chi phí tối đa nhưng vẫn đủ năng lượng",
                bestSavings, targetCalo, dailyBudget, applyDate));

        result.add(createSuggestedPlan("plan_3", "Thực đơn Giàu Đạm & Tăng cường Cơ",
                "Tối ưu hóa đạm tự nhiên, hỗ trợ thể trạng",
                bestProtein, targetCalo, dailyBudget, applyDate));

        return result;
    }

    private boolean isSameCombo(MenuCombo a, MenuCombo b) {
        return a.sang.getId().equals(b.sang.getId()) &&
               a.trua.getId().equals(b.trua.getId()) &&
               a.toi.getId().equals(b.toi.getId());
    }

    private SuggestedMenuResponse createSuggestedPlan(String planId, String title, String subtitle,
                                                      MenuCombo c, double targetCalo, double dailyBudget,
                                                      LocalDate applyDate) {
        int savings = Math.max(0, (int)(dailyBudget - c.cost));
        double diffCalo = Math.abs(c.calo - targetCalo) / targetCalo;
        double diffCost = c.cost <= dailyBudget ? 0 : (c.cost - dailyBudget) / dailyBudget;
        int matchRate = (int) Math.round((1.0 - (diffCalo * 0.6 + diffCost * 0.4)) * 100);
        if (matchRate > 98) matchRate = 98;
        if (matchRate < 75) matchRate = 75;

        List<MenuDetailResponse> details = new ArrayList<>();
        details.add(toDetailResponse(c.sang, "SANG"));
        details.add(toDetailResponse(c.trua, "TRUA"));
        details.add(toDetailResponse(c.toi, "TOI"));

        return SuggestedMenuResponse.builder()
                .planId(planId)
                .tenThucDon(title)
                .moTa(subtitle)
                .tiLePhuHop(matchRate)
                .soTienTietKiem(savings)
                .tongCalo(Math.round(c.calo * 10.0) / 10.0)
                .tongProtein(Math.round(c.protein * 10.0) / 10.0)
                .tongCarb(Math.round(c.carb * 10.0) / 10.0)
                .tongFat(Math.round(c.fat * 10.0) / 10.0)
                .tongChiPhi(BigDecimal.valueOf(c.cost))
                .soBua(details.size())
                .ngayApDung(applyDate)
                .chiTietThucDon(details)
                .build();
    }

    private MenuDetailResponse toDetailResponse(Dish d, String buaAnCode) {
        return MenuDetailResponse.builder()
                .monAnId(d.getId())
                .tenMon(d.getTenMon())
                .buaAn(buaAnCode)
                .soLuong(1.0)
                .calo(d.getCalo())
                .chiPhi(d.getGiaDuKien())
                .hinhAnh(d.getHinhAnh())
                .khauPhan(d.getKhauPhan())
                .protein(d.getProtein())
                .carb(d.getCarb())
                .fat(d.getFat())
                .build();
    }

    
    public MenuCostResponse getMenuCost(Long menuId, String userEmail) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay thuc don voi ID: " + menuId));

        User user = menu.getNguoiDung();
        if (user == null && userEmail != null) {
            user = userRepository.findByEmail(userEmail).orElse(null);
        }

        BigDecimal dailyBudget = BigDecimal.valueOf(100000.0);
        if (user != null) {
            List<Budget> budgets = budgetRepository.findByNguoiDungId(user.getId());
            if (!budgets.isEmpty() && budgets.get(0).getNganSachNgay() != null) {
                dailyBudget = budgets.get(0).getNganSachNgay();
            }
        }

        BigDecimal tongChiPhi = BigDecimal.ZERO;
        Map<String, BigDecimal> chiPhiTheoBua = new LinkedHashMap<>();
        chiPhiTheoBua.put("SANG", BigDecimal.ZERO);
        chiPhiTheoBua.put("TRUA", BigDecimal.ZERO);
        chiPhiTheoBua.put("TOI", BigDecimal.ZERO);
        chiPhiTheoBua.put("PHU", BigDecimal.ZERO);

        if (menu.getChiTietThucDon() != null) {
            for (MenuDetail d : menu.getChiTietThucDon()) {
                BigDecimal itemCost = d.getChiPhi();
                if (itemCost == null && d.getMonAn() != null && d.getMonAn().getGiaDuKien() != null) {
                    double qty = d.getSoLuong() != null ? d.getSoLuong() : 1.0;
                    itemCost = d.getMonAn().getGiaDuKien().multiply(BigDecimal.valueOf(qty));
                }
                if (itemCost == null) itemCost = BigDecimal.ZERO;

                tongChiPhi = tongChiPhi.add(itemCost);

                String bua = d.getBuaAn() != null ? d.getBuaAn().toUpperCase() : "TRUA";
                chiPhiTheoBua.put(bua, chiPhiTheoBua.getOrDefault(bua, BigDecimal.ZERO).add(itemCost));
            }
        }

        BigDecimal tienConLai = BigDecimal.ZERO;
        BigDecimal soTienVuot = BigDecimal.ZERO;
        String trangThai = "TRONG_NGAN_SACH";

        if (tongChiPhi.compareTo(dailyBudget) <= 0) {
            tienConLai = dailyBudget.subtract(tongChiPhi);
            soTienVuot = BigDecimal.ZERO;
            trangThai = "TRONG_NGAN_SACH";
        } else {
            tienConLai = BigDecimal.ZERO;
            soTienVuot = tongChiPhi.subtract(dailyBudget);
            trangThai = "VUOT_NGAN_SACH";
        }

        double tyLeSuDung = 0.0;
        if (dailyBudget.compareTo(BigDecimal.ZERO) > 0) {
            tyLeSuDung = Math.round((tongChiPhi.doubleValue() / dailyBudget.doubleValue() * 100.0) * 100.0) / 100.0;
        }

        return MenuCostResponse.builder()
                .menuId(menu.getId())
                .tenThucDon(menu.getTenThucDon())
                .tongChiPhi(tongChiPhi)
                .nganSachNgay(dailyBudget)
                .tienConLai(tienConLai)
                .soTienVuot(soTienVuot)
                .tyLeSuDung(tyLeSuDung)
                .chiPhiTheoBua(chiPhiTheoBua)
                .trangThai(trangThai)
                .build();
    }

    public MenuResponse toResponse(Menu m) {
        List<MenuDetailResponse> details = new ArrayList<>();
        if (m.getChiTietThucDon() != null) {
            details = m.getChiTietThucDon().stream().map(d -> MenuDetailResponse.builder()
                    .id(d.getId())
                    .monAnId(d.getMonAn().getId())
                    .tenMon(d.getMonAn().getTenMon())
                    .buaAn(d.getBuaAn())
                    .soLuong(d.getSoLuong())
                    .calo(d.getCalo())
                    .chiPhi(d.getChiPhi())
                    .hinhAnh(d.getMonAn().getHinhAnh())
                    .khauPhan(d.getMonAn().getKhauPhan())
                    .protein(d.getMonAn().getProtein())
                    .carb(d.getMonAn().getCarb())
                    .fat(d.getMonAn().getFat())
                    .build()).collect(Collectors.toList());
        }

        return MenuResponse.builder()
                .id(m.getId())
                .userId(m.getNguoiDung().getId())
                .tenThucDon(m.getTenThucDon())
                .ngayApDung(m.getNgayApDung())
                .tongCalo(m.getTongCalo())
                .tongProtein(m.getTongProtein())
                .tongCarb(m.getTongCarb())
                .tongFat(m.getTongFat())
                .tongChiPhi(m.getTongChiPhi())
                .trangThai(m.getTrangThai())
                .createdAt(m.getCreatedAt())
                .chiTietThucDon(details)
                .build();
    }

    private static class MenuCombo {
        Dish sang;
        Dish trua;
        Dish toi;
        double calo;
        double protein;
        double carb;
        double fat;
        double cost;
        double balancedScore;
        double savingsScore;
        double proteinScore;
    }

    public MenuIngredientsResponse getMenuIngredients(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay thuc don voi ID: " + menuId));

        List<MenuDetail> details = menu.getChiTietThucDon();
        if (details == null) {
            details = Collections.emptyList();
        }

        // Map to aggregate ingredients by ingredient ID
        Map<Long, IngredientAggregate> aggregateMap = new LinkedHashMap<>();

        for (MenuDetail detail : details) {
            if (detail.getMonAn() == null) continue;
            Long monAnId = detail.getMonAn().getId();
            double portion = (detail.getSoLuong() != null && detail.getSoLuong() > 0) ? detail.getSoLuong() : 1.0;

            List<DishIngredient> dishIngredients = dishIngredientRepository.findByMonAnId(monAnId);
            if (dishIngredients == null) continue;

            for (DishIngredient di : dishIngredients) {
                if (di.getNguyenLieu() == null) continue;
                Ingredient ing = di.getNguyenLieu();
                Long ingId = ing.getId();
                double qty = (di.getSoLuong() != null ? di.getSoLuong() : 1.0) * portion;

                if (!aggregateMap.containsKey(ingId)) {
                    aggregateMap.put(ingId, new IngredientAggregate(
                            ingId,
                            ing.getTenNguyenLieu(),
                            di.getDonVi() != null ? di.getDonVi() : ing.getDonVi(),
                            ing.getGia() != null ? ing.getGia() : BigDecimal.ZERO,
                            qty
                    ));
                } else {
                    IngredientAggregate agg = aggregateMap.get(ingId);
                    agg.soLuong += qty;
                }
            }
        }

        List<IngredientItemResponse> items = new ArrayList<>();
        BigDecimal tongChiPhi = BigDecimal.ZERO;

        for (IngredientAggregate agg : aggregateMap.values()) {
            double roundedQty = Math.round(agg.soLuong * 100.0) / 100.0;
            BigDecimal qtyBd = BigDecimal.valueOf(roundedQty);
            BigDecimal thanhTien = agg.donGia.multiply(qtyBd);

            if (thanhTien.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
                thanhTien = thanhTien.setScale(0, RoundingMode.HALF_UP);
            } else {
                thanhTien = thanhTien.setScale(2, RoundingMode.HALF_UP);
            }

            BigDecimal donGiaFormatted = agg.donGia;
            if (donGiaFormatted.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
                donGiaFormatted = donGiaFormatted.setScale(0, RoundingMode.HALF_UP);
            } else {
                donGiaFormatted = donGiaFormatted.setScale(2, RoundingMode.HALF_UP);
            }

            tongChiPhi = tongChiPhi.add(thanhTien);

            items.add(IngredientItemResponse.builder()
                    .id(agg.id)
                    .tenNguyenLieu(agg.tenNguyenLieu)
                    .soLuong(roundedQty)
                    .donVi(agg.donVi)
                    .donGia(donGiaFormatted)
                    .thanhTien(thanhTien)
                    .build());
        }

        if (tongChiPhi.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
            tongChiPhi = tongChiPhi.setScale(0, RoundingMode.HALF_UP);
        } else {
            tongChiPhi = tongChiPhi.setScale(2, RoundingMode.HALF_UP);
        }

        return MenuIngredientsResponse.builder()
                .menuId(menu.getId())
                .tenThucDon(menu.getTenThucDon())
                .tongChiPhiNguyenLieu(tongChiPhi)
                .nguyenLieu(items)
                .build();
    }

    private static class IngredientAggregate {
        Long id;
        String tenNguyenLieu;
        String donVi;
        BigDecimal donGia;
        double soLuong;

        IngredientAggregate(Long id, String tenNguyenLieu, String donVi, BigDecimal donGia, double soLuong) {
            this.id = id;
            this.tenNguyenLieu = tenNguyenLieu;
            this.donVi = donVi;
            this.donGia = donGia;
            this.soLuong = soLuong;
        }
    }
}
