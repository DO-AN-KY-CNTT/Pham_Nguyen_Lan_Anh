package com.example.phamnguyenlananh.ui.meal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.data.api.ApiClient;
import com.example.phamnguyenlananh.data.api.NutriBudgetApiService;
import com.example.phamnguyenlananh.data.api.TokenManager;
import com.example.phamnguyenlananh.data.api.model.ApiResponse;
import com.example.phamnguyenlananh.data.api.model.MenuCostResponse;
import com.example.phamnguyenlananh.data.api.model.MenuDetailResponse;
import com.example.phamnguyenlananh.data.api.model.MenuResponse;
import com.example.phamnguyenlananh.data.api.model.NutritionGoalResponse;
import com.example.phamnguyenlananh.model.MealItem;
import com.example.phamnguyenlananh.model.MealPlan;
import com.example.phamnguyenlananh.ui.auth.LoginActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CostOverviewActivity extends AppCompatActivity {
    private static final String TAG = "CostOverviewActivity";

    private ImageView btnBackCost;
    private TextView tvCostMenuName;
    private TextView tvCostSpentBig, tvCostLimitBig, tvCostSurplusBadge;
    private TextView tvCostBudgetUsagePercent;
    private MaterialCardView cardCostOverBudgetWarning;
    private TextView tvCostWarningTitle, tvCostWarningDesc;
    private Button btnCostAdjustMeal;

    private TextView tvCostCaloSummary, tvCostProteinSummary, tvCostCarbsSummary, tvCostFatSummary;
    private LinearProgressIndicator progressCostBudget, progressCostCalo;

    private LinearLayout layoutCostBreakfast, layoutCostLunch, layoutCostDinner, layoutCostSnack;
    private TextView tvCostBreakfastName, tvCostBreakfastCost;
    private TextView tvCostLunchName, tvCostLunchCost;
    private TextView tvCostDinnerName, tvCostDinnerCost;
    private TextView tvCostSnackName, tvCostSnackCost;
    private View dividerDinner;

    private Button btnCostToGrocery;

    private NutriBudgetApiService apiService;
    private TokenManager tokenManager;
    private long menuId = 0;
    private MealPlan currentPlan;
    private double targetCalo = 2000;
    private double targetProtein = 75;
    private double targetCarbs = 250;
    private double targetFat = 55;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_overview);

        apiService = ApiClient.getNutriBudgetApiService(this);
        tokenManager = TokenManager.getInstance(this);

        if (getIntent().hasExtra("EXTRA_MEAL_PLAN")) {
            currentPlan = (MealPlan) getIntent().getSerializableExtra("EXTRA_MEAL_PLAN");
        }
        menuId = getIntent().getLongExtra("EXTRA_MENU_ID", 0);
        if (menuId <= 0 && currentPlan != null) {
            try {
                menuId = Long.parseLong(currentPlan.getId());
            } catch (Exception ignored) {}
        }

        initViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNutritionGoals();
        if (menuId > 0) {
            loadCostData(menuId);
            loadMenuDetails(menuId);
        } else {
            resolveUserMenuAndLoad();
        }
    }

    private void initViews() {
        btnBackCost = findViewById(R.id.btnBackCost);
        tvCostMenuName = findViewById(R.id.tvCostMenuName);

        cardCostOverBudgetWarning = findViewById(R.id.cardCostOverBudgetWarning);
        tvCostWarningTitle = findViewById(R.id.tvCostWarningTitle);
        tvCostWarningDesc = findViewById(R.id.tvCostWarningDesc);
        btnCostAdjustMeal = findViewById(R.id.btnCostAdjustMeal);

        tvCostSpentBig = findViewById(R.id.tvCostSpentBig);
        tvCostLimitBig = findViewById(R.id.tvCostLimitBig);
        tvCostSurplusBadge = findViewById(R.id.tvCostSurplusBadge);
        progressCostBudget = findViewById(R.id.progressCostBudget);
        tvCostBudgetUsagePercent = findViewById(R.id.tvCostBudgetUsagePercent);

        tvCostCaloSummary = findViewById(R.id.tvCostCaloSummary);
        progressCostCalo = findViewById(R.id.progressCostCalo);
        tvCostProteinSummary = findViewById(R.id.tvCostProteinSummary);
        tvCostCarbsSummary = findViewById(R.id.tvCostCarbsSummary);
        tvCostFatSummary = findViewById(R.id.tvCostFatSummary);

        layoutCostBreakfast = findViewById(R.id.layoutCostBreakfast);
        tvCostBreakfastName = findViewById(R.id.tvCostBreakfastName);
        tvCostBreakfastCost = findViewById(R.id.tvCostBreakfastCost);

        layoutCostLunch = findViewById(R.id.layoutCostLunch);
        tvCostLunchName = findViewById(R.id.tvCostLunchName);
        tvCostLunchCost = findViewById(R.id.tvCostLunchCost);

        layoutCostDinner = findViewById(R.id.layoutCostDinner);
        tvCostDinnerName = findViewById(R.id.tvCostDinnerName);
        tvCostDinnerCost = findViewById(R.id.tvCostDinnerCost);

        dividerDinner = findViewById(R.id.dividerDinner);
        layoutCostSnack = findViewById(R.id.layoutCostSnack);
        tvCostSnackName = findViewById(R.id.tvCostSnackName);
        tvCostSnackCost = findViewById(R.id.tvCostSnackCost);

        btnCostToGrocery = findViewById(R.id.btnCostToGrocery);
    }

    private void setupListeners() {
        btnBackCost.setOnClickListener(v -> finish());

        btnCostToGrocery.setOnClickListener(v -> {
            Intent intent = new Intent(this, GroceryListActivity.class);
            if (menuId > 0) {
                intent.putExtra("EXTRA_MENU_ID", menuId);
            }
            if (currentPlan != null) {
                intent.putExtra("EXTRA_MEAL_PLAN", currentPlan);
            }
            startActivity(intent);
        });

        btnCostAdjustMeal.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdjustMealActivity.class);
            if (currentPlan != null) {
                intent.putExtra("EXTRA_MEAL_PLAN", currentPlan);
            }
            startActivity(intent);
        });
    }

    private void resolveUserMenuAndLoad() {
        long userId = tokenManager.getUserId();
        if (userId <= 0) {
            redirectToLogin("Vui lòng đăng nhập lại.");
            return;
        }

        apiService.getMenus(userId).enqueue(new Callback<ApiResponse<List<MenuResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<MenuResponse>>> call, Response<ApiResponse<List<MenuResponse>>> response) {
                if (response.code() == 401 || response.code() == 403) {
                    redirectToLogin("Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.");
                    return;
                }
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<MenuResponse> menus = response.body().getData();
                    if (!menus.isEmpty()) {
                        menuId = menus.get(0).getId();
                        loadCostData(menuId);
                        loadMenuDetails(menuId);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<MenuResponse>>> call, Throwable t) {
                Log.e(TAG, "Error fetching user menus: " + t.getMessage());
            }
        });
    }

    private void loadNutritionGoals() {
        long userId = tokenManager.getUserId();
        if (userId <= 0) {
            redirectToLogin("Vui lòng đăng nhập lại.");
            return;
        }

        apiService.getNutritionGoals(userId).enqueue(new Callback<ApiResponse<List<NutritionGoalResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<NutritionGoalResponse>>> call, Response<ApiResponse<List<NutritionGoalResponse>>> response) {
                if (response.code() == 401 || response.code() == 403) {
                    redirectToLogin("Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.");
                    return;
                }
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<NutritionGoalResponse> goals = response.body().getData();
                    if (!goals.isEmpty()) {
                        NutritionGoalResponse g = goals.get(0);
                        if (g.getCaloMucTieu() != null && g.getCaloMucTieu() > 0) targetCalo = g.getCaloMucTieu();
                        if (g.getProteinMucTieu() != null && g.getProteinMucTieu() > 0) targetProtein = g.getProteinMucTieu();
                        if (g.getCarbMucTieu() != null && g.getCarbMucTieu() > 0) targetCarbs = g.getCarbMucTieu();
                        if (g.getFatMucTieu() != null && g.getFatMucTieu() > 0) targetFat = g.getFatMucTieu();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<NutritionGoalResponse>>> call, Throwable t) {
                Log.w(TAG, "Could not load nutrition goals: " + t.getMessage());
            }
        });
    }

    private void loadCostData(long targetMenuId) {
        apiService.getMenuCost(targetMenuId).enqueue(new Callback<ApiResponse<MenuCostResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<MenuCostResponse>> call, Response<ApiResponse<MenuCostResponse>> response) {
                if (response.code() == 401 || response.code() == 403) {
                    redirectToLogin("Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.");
                    return;
                }

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    MenuCostResponse cost = response.body().getData();
                    bindCostData(cost);
                } else {
                    Toast.makeText(CostOverviewActivity.this, "Không thể tải chi phí thực đơn từ hệ thống", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<MenuCostResponse>> call, Throwable t) {
                Log.e(TAG, "loadCostData onFailure: " + t.getMessage());
                Toast.makeText(CostOverviewActivity.this, "Lỗi kết nối khi tải tổng chi phí", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindCostData(MenuCostResponse cost) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));

        // 1. Tên thực đơn
        if (cost.getTenThucDon() != null && !cost.getTenThucDon().isEmpty()) {
            tvCostMenuName.setText(cost.getTenThucDon());
        }

        // 2. Chi phí & Ngân sách
        double tongChiPhi = cost.getTongChiPhi().doubleValue();
        double nganSachNgay = cost.getNganSachNgay().doubleValue();
        double tienConLai = cost.getTienConLai().doubleValue();
        double soTienVuot = cost.getSoTienVuot().doubleValue();
        double tyLeSuDung = cost.getTyLeSuDung();

        tvCostSpentBig.setText(nf.format(tongChiPhi) + "đ");
        tvCostLimitBig.setText("/ " + nf.format(nganSachNgay) + "đ");

        // 3. Tỷ lệ sử dụng
        tvCostBudgetUsagePercent.setText(String.format(Locale.getDefault(), "Đã sử dụng %.1f%% hạn mức ngân sách ngày", tyLeSuDung));
        progressCostBudget.setProgress(Math.min(100, (int) Math.round(tyLeSuDung)));

        // 4. So sánh chi phí với ngân sách
        boolean isOverBudget = "VUOT_NGAN_SACH".equals(cost.getTrangThai()) || tongChiPhi > nganSachNgay;
        if (isOverBudget) {
            tvCostSurplusBadge.setBackgroundResource(R.drawable.bg_badge_red);
            tvCostSurplusBadge.setTextColor(ContextCompat.getColor(this, R.color.badge_red_text));
            tvCostSurplusBadge.setText("Vượt " + nf.format(soTienVuot) + "đ");

            progressCostBudget.setIndicatorColor(ContextCompat.getColor(this, R.color.error));

            cardCostOverBudgetWarning.setVisibility(View.VISIBLE);
            tvCostWarningDesc.setText("Tổng chi phí (" + nf.format(tongChiPhi) + "đ) đã vượt ngân sách ngày (" + nf.format(nganSachNgay) + "đ) là " + nf.format(soTienVuot) + "đ. Bạn có thể điều chỉnh lại thực đơn để tiết kiệm chi phí.");
        } else {
            tvCostSurplusBadge.setBackgroundResource(R.drawable.bg_badge_green);
            tvCostSurplusBadge.setTextColor(ContextCompat.getColor(this, R.color.badge_green_text));
            int remainingPercent = (int) Math.round(nganSachNgay > 0 ? (tienConLai / nganSachNgay * 100) : 0);
            tvCostSurplusBadge.setText("Dư " + nf.format(tienConLai) + "đ (" + remainingPercent + "%)");

            progressCostBudget.setIndicatorColor(ContextCompat.getColor(this, R.color.primary));

            cardCostOverBudgetWarning.setVisibility(View.GONE);
        }

        // 5. Chi phí theo bữa
        Map<String, BigDecimal> mealCosts = cost.getChiPhiTheoBua();
        double breakfastCost = (mealCosts != null && mealCosts.containsKey("SANG") && mealCosts.get("SANG") != null) ? mealCosts.get("SANG").doubleValue() : 0;
        double lunchCost = (mealCosts != null && mealCosts.containsKey("TRUA") && mealCosts.get("TRUA") != null) ? mealCosts.get("TRUA").doubleValue() : 0;
        double dinnerCost = (mealCosts != null && mealCosts.containsKey("TOI") && mealCosts.get("TOI") != null) ? mealCosts.get("TOI").doubleValue() : 0;
        double snackCost = (mealCosts != null && mealCosts.containsKey("PHU") && mealCosts.get("PHU") != null) ? mealCosts.get("PHU").doubleValue() : 0;

        int bfPercent = tongChiPhi > 0 ? (int) Math.round(breakfastCost / tongChiPhi * 100) : 0;
        int luPercent = tongChiPhi > 0 ? (int) Math.round(lunchCost / tongChiPhi * 100) : 0;
        int diPercent = tongChiPhi > 0 ? (int) Math.round(dinnerCost / tongChiPhi * 100) : 0;
        int snPercent = tongChiPhi > 0 ? (int) Math.round(snackCost / tongChiPhi * 100) : 0;

        tvCostBreakfastCost.setText(nf.format(breakfastCost) + "đ (" + bfPercent + "%)");
        tvCostLunchCost.setText(nf.format(lunchCost) + "đ (" + luPercent + "%)");
        tvCostDinnerCost.setText(nf.format(dinnerCost) + "đ (" + diPercent + "%)");

        if (snackCost > 0) {
            layoutCostSnack.setVisibility(View.VISIBLE);
            dividerDinner.setVisibility(View.VISIBLE);
            tvCostSnackCost.setText(nf.format(snackCost) + "đ (" + snPercent + "%)");
        } else {
            layoutCostSnack.setVisibility(View.GONE);
            dividerDinner.setVisibility(View.GONE);
        }
    }

    private void loadMenuDetails(long targetMenuId) {
        apiService.getMenuById(targetMenuId).enqueue(new Callback<ApiResponse<MenuResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<MenuResponse>> call, Response<ApiResponse<MenuResponse>> response) {
                if (response.code() == 401 || response.code() == 403) {
                    redirectToLogin("Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.");
                    return;
                }
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    MenuResponse menu = response.body().getData();
                    bindMenuDetails(menu);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<MenuResponse>> call, Throwable t) {
                Log.e(TAG, "loadMenuDetails onFailure: " + t.getMessage());
            }
        });
    }

    private void bindMenuDetails(MenuResponse menu) {
        // Dinh dưỡng
        double calo = menu.getTongCalo() != null ? menu.getTongCalo() : 0;
        double protein = menu.getTongProtein() != null ? menu.getTongProtein() : 0;
        double carbs = menu.getTongCarb() != null ? menu.getTongCarb() : 0;
        double fat = menu.getTongFat() != null ? menu.getTongFat() : 0;

        int caloPercent = (int) Math.round(targetCalo > 0 ? (calo / targetCalo * 100) : 0);
        tvCostCaloSummary.setText(String.format(Locale.getDefault(), "%,.0f / %,.0f kcal (%d%%)", calo, targetCalo, caloPercent));
        progressCostCalo.setProgress(Math.min(100, caloPercent));

        tvCostProteinSummary.setText(String.format(Locale.getDefault(), "%.0f / %.0fg", protein, targetProtein));
        tvCostCarbsSummary.setText(String.format(Locale.getDefault(), "%.0f / %.0fg", carbs, targetCarbs));
        tvCostFatSummary.setText(String.format(Locale.getDefault(), "%.0f / %.0fg", fat, targetFat));

        // Tên các món theo bữa
        List<MenuDetailResponse> details = menu.getChiTietThucDon();
        if (details != null && !details.isEmpty()) {
            String sangName = "";
            String truaName = "";
            String toiName = "";
            String phuName = "";

            for (MenuDetailResponse d : details) {
                if (d.getBuaAn() != null) {
                    String bua = d.getBuaAn().toUpperCase();
                    if (bua.contains("SANG")) {
                        sangName = (sangName.isEmpty() ? "" : sangName + ", ") + d.getTenMon();
                    } else if (bua.contains("TRUA")) {
                        truaName = (truaName.isEmpty() ? "" : truaName + ", ") + d.getTenMon();
                    } else if (bua.contains("TOI")) {
                        toiName = (toiName.isEmpty() ? "" : toiName + ", ") + d.getTenMon();
                    } else if (bua.contains("PHU")) {
                        phuName = (phuName.isEmpty() ? "" : phuName + ", ") + d.getTenMon();
                    }
                }
            }

            if (!sangName.isEmpty()) tvCostBreakfastName.setText("Bữa sáng (" + sangName + ")");
            if (!truaName.isEmpty()) tvCostLunchName.setText("Bữa trưa (" + truaName + ")");
            if (!toiName.isEmpty()) tvCostDinnerName.setText("Bữa tối (" + toiName + ")");
            if (!phuName.isEmpty()) {
                tvCostSnackName.setText("Bữa phụ (" + phuName + ")");
                layoutCostSnack.setVisibility(View.VISIBLE);
                dividerDinner.setVisibility(View.VISIBLE);
            }
        }
    }

    private void redirectToLogin(String message) {
        tokenManager.clearSession();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}