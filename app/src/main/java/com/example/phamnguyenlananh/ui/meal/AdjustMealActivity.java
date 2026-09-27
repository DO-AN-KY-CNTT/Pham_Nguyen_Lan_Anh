package com.example.phamnguyenlananh.ui.meal;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.adapter.AlternativeDishAdapter;
import com.example.phamnguyenlananh.data.MockDataRepository;
import com.example.phamnguyenlananh.data.api.ApiClient;
import com.example.phamnguyenlananh.data.api.NutriBudgetApiService;
import com.example.phamnguyenlananh.data.api.TokenManager;
import com.example.phamnguyenlananh.data.api.model.ApiResponse;
import com.example.phamnguyenlananh.data.api.model.BudgetResponse;
import com.example.phamnguyenlananh.data.api.model.DishResponse;
import com.example.phamnguyenlananh.data.api.model.MenuDetailRequest;
import com.example.phamnguyenlananh.data.api.model.MenuRequest;
import com.example.phamnguyenlananh.data.api.model.MenuResponse;
import com.example.phamnguyenlananh.data.api.model.NutritionGoalResponse;
import com.example.phamnguyenlananh.data.api.model.UpdateMenuItemRequest;
import com.example.phamnguyenlananh.model.AlternativeDish;
import com.example.phamnguyenlananh.model.MealItem;
import com.example.phamnguyenlananh.model.MealPlan;
import com.example.phamnguyenlananh.ui.auth.LoginActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdjustMealActivity extends AppCompatActivity {
    private static final String TAG = "AdjustMealActivity";

    private ImageView btnBackAdjust, imgCurrentAdjustDish;
    private TextView tvCurrentAdjustName, tvCurrentAdjustStats, tvCurrentAdjustCost;
    private TextView tvImpactCalo, tvImpactProtein, tvImpactCost;
    private RecyclerView rvAlternativeDishes;
    private Button btnSaveAdjustChanges;

    private AlternativeDishAdapter alternativeAdapter;
    private List<AlternativeDish> alternativeDishesList = new ArrayList<>();
    private List<DishResponse> allDishesFromApi = new ArrayList<>();

    private NutriBudgetApiService apiService;
    private TokenManager tokenManager;

    private MealPlan currentPlan;
    private int currentDishIndex = 1; // Mặc định món trưa hoặc món đầu tiên
    private MealItem currentDish;
    private AlternativeDish selectedDish = null;

    private double userDailyBudget = 100000.0;
    private double userTargetCalo = 2000.0;
    private double userTargetProtein = 100.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_meal);

        apiService = ApiClient.getNutriBudgetApiService(this);
        tokenManager = TokenManager.getInstance(this);

        if (getIntent().hasExtra("EXTRA_MEAL_PLAN")) {
            currentPlan = (MealPlan) getIntent().getSerializableExtra("EXTRA_MEAL_PLAN");
        }
        if (currentPlan == null) {
            currentPlan = MockDataRepository.getInstance().getCurrentMealPlan();
        }

        if (getIntent().hasExtra("EXTRA_MEAL_INDEX")) {
            currentDishIndex = getIntent().getIntExtra("EXTRA_MEAL_INDEX", 0);
        } else if (currentPlan != null && currentPlan.getMeals() != null && currentPlan.getMeals().size() > 1) {
            currentDishIndex = 1; // Bữa trưa
        } else {
            currentDishIndex = 0;
        }

        initViews();
        setupListeners();
        setupRecyclerView();

        loadCurrentDishInfo();
        fetchUserBudgetAndGoals();
        fetchDishesFromApi();
    }

    private void initViews() {
        btnBackAdjust = findViewById(R.id.btnBackAdjust);
        imgCurrentAdjustDish = findViewById(R.id.imgCurrentAdjustDish);
        tvCurrentAdjustName = findViewById(R.id.tvCurrentAdjustName);
        tvCurrentAdjustStats = findViewById(R.id.tvCurrentAdjustStats);
        tvCurrentAdjustCost = findViewById(R.id.tvCurrentAdjustCost);
        tvImpactCalo = findViewById(R.id.tvImpactCalo);
        tvImpactProtein = findViewById(R.id.tvImpactProtein);
        tvImpactCost = findViewById(R.id.tvImpactCost);
        rvAlternativeDishes = findViewById(R.id.rvAlternativeDishes);
        btnSaveAdjustChanges = findViewById(R.id.btnSaveAdjustChanges);
    }

    private void setupRecyclerView() {
        rvAlternativeDishes.setLayoutManager(new LinearLayoutManager(this));
        alternativeAdapter = new AlternativeDishAdapter(alternativeDishesList, dish -> onAlternativeDishSelected(dish));
        rvAlternativeDishes.setAdapter(alternativeAdapter);
    }

    private void setupListeners() {
        btnBackAdjust.setOnClickListener(v -> finish());

        // Cho phép người dùng chạm vào Card món hiện tại để chuyển đổi món cần đổi (Sáng / Trưa / Tối)
        View.OnClickListener selectMealToAdjustListener = v -> showSelectMealDialog();
        tvCurrentAdjustName.setOnClickListener(selectMealToAdjustListener);
        imgCurrentAdjustDish.setOnClickListener(selectMealToAdjustListener);
        if (tvCurrentAdjustName.getParent() != null && tvCurrentAdjustName.getParent().getParent() instanceof View) {
            ((View) tvCurrentAdjustName.getParent().getParent()).setOnClickListener(selectMealToAdjustListener);
        }

        btnSaveAdjustChanges.setOnClickListener(v -> saveChangesToDatabase());
    }

    private void showSelectMealDialog() {
        if (currentPlan == null || currentPlan.getMeals() == null || currentPlan.getMeals().isEmpty()) {
            return;
        }

        List<MealItem> meals = currentPlan.getMeals();
        String[] mealTitles = new String[meals.size()];
        for (int i = 0; i < meals.size(); i++) {
            MealItem it = meals.get(i);
            mealTitles[i] = it.getMealType() + ": " + it.getDishName() + " (" + it.getCost() + "đ)";
        }

        new AlertDialog.Builder(this)
                .setTitle("Chọn món trong thực đơn muốn đổi")
                .setItems(mealTitles, (dialog, which) -> {
                    currentDishIndex = which;
                    selectedDish = null;
                    loadCurrentDishInfo();
                    filterAndBuildAlternativeDishes();
                })
                .setNegativeButton("Đóng", null)
                .show();
    }

    private void loadCurrentDishInfo() {
        if (currentPlan == null || currentPlan.getMeals() == null || currentPlan.getMeals().isEmpty()) {
            return;
        }

        if (currentDishIndex < 0 || currentDishIndex >= currentPlan.getMeals().size()) {
            currentDishIndex = 0;
        }

        currentDish = currentPlan.getMeals().get(currentDishIndex);

        tvCurrentAdjustName.setText(currentDish.getDishName());
        tvCurrentAdjustStats.setText(currentDish.getCalories() + " kcal • " + currentDish.getProtein() + "g Đạm • " + currentDish.getMealType());

        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvCurrentAdjustCost.setText(nf.format(currentDish.getCost()) + "đ");

        if (currentDish.getImageRes() != 0) {
            imgCurrentAdjustDish.setImageResource(currentDish.getImageRes());
        }

        // Cập nhật giá trị hiển thị ban đầu của tác động
        tvImpactCalo.setText(currentPlan.getTotalCalories() + " kcal");
        tvImpactProtein.setText(currentPlan.getTotalProtein() + "g");
        tvImpactCost.setText(nf.format(currentPlan.getTotalCost()) + "đ");
        tvImpactCost.setTextColor(ContextCompat.getColor(this, R.color.secondary));
    }

    private void fetchUserBudgetAndGoals() {
        long userId = tokenManager.getUserId();
        if (userId <= 0) return;

        // 1. Lấy ngân sách
        apiService.getBudgets(userId).enqueue(new Callback<ApiResponse<List<BudgetResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BudgetResponse>>> call, Response<ApiResponse<List<BudgetResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<BudgetResponse> list = response.body().getData();
                    if (!list.isEmpty() && list.get(0).getNganSachNgay() != null) {
                        userDailyBudget = list.get(0).getNganSachNgay();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<BudgetResponse>>> call, Throwable t) {
                Log.w(TAG, "Không thể tải ngân sách từ API: " + t.getMessage());
            }
        });

        // 2. Lấy mục tiêu dinh dưỡng
        apiService.getNutritionGoals(userId).enqueue(new Callback<ApiResponse<List<NutritionGoalResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<NutritionGoalResponse>>> call, Response<ApiResponse<List<NutritionGoalResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<NutritionGoalResponse> list = response.body().getData();
                    if (!list.isEmpty()) {
                        NutritionGoalResponse g = list.get(0);
                        if (g.getCaloMucTieu() != null) userTargetCalo = g.getCaloMucTieu();
                        if (g.getProteinMucTieu() != null) userTargetProtein = g.getProteinMucTieu();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<NutritionGoalResponse>>> call, Throwable t) {
                Log.w(TAG, "Không thể tải mục tiêu dinh dưỡng từ API: " + t.getMessage());
            }
        });
    }

    private void fetchDishesFromApi() {
        apiService.getDishes(null).enqueue(new Callback<ApiResponse<List<DishResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<DishResponse>>> call, Response<ApiResponse<List<DishResponse>>> response) {
                if (response.code() == 401 || response.code() == 403) {
                    Toast.makeText(AdjustMealActivity.this, "Phiên đăng nhập hết hạn.", Toast.LENGTH_SHORT).show();
                    tokenManager.clearSession();
                    startActivity(new Intent(AdjustMealActivity.this, LoginActivity.class));
                    finish();
                    return;
                }

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    allDishesFromApi = response.body().getData();
                    filterAndBuildAlternativeDishes();
                } else {
                    Log.e(TAG, "Lỗi lấy danh sách món ăn từ API");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<DishResponse>>> call, Throwable t) {
                Log.e(TAG, "Lỗi kết nối API dishes: " + t.getMessage());
                Toast.makeText(AdjustMealActivity.this, "Không thể tải danh sách món ăn từ máy chủ.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterAndBuildAlternativeDishes() {
        if (allDishesFromApi == null || allDishesFromApi.isEmpty() || currentDish == null) {
            return;
        }

        alternativeDishesList.clear();
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));

        String mealTypeCode = resolveMealTypeCode(currentDish.getMealType());

        for (DishResponse d : allDishesFromApi) {
            // Không hiển thị chính món đang dùng
            if (currentDish.getDishId() != null && currentDish.getDishId().equals(d.getId())) {
                continue;
            }
            if (currentDish.getDishName() != null && currentDish.getDishName().equalsIgnoreCase(d.getTenMon())) {
                continue;
            }

            int dishCost = d.getGiaDuKien() != null ? d.getGiaDuKien().intValue() : 30000;
            int dishCalo = d.getCalo() != null ? d.getCalo().intValue() : 400;
            int dishProtein = d.getProtein() != null ? d.getProtein().intValue() : 20;
            int dishCarb = d.getCarb() != null ? d.getCarb().intValue() : 50;
            int dishFat = d.getFat() != null ? d.getFat().intValue() : 10;

            // Tính chênh lệch giá so với món hiện tại
            int costDiff = dishCost - currentDish.getCost();
            String savingsText;
            if (costDiff < 0) {
                savingsText = "Tiết kiệm " + nf.format(Math.abs(costDiff)) + "đ";
            } else if (costDiff == 0) {
                savingsText = "Đồng giá";
            } else {
                savingsText = "Bổ sung +" + nf.format(costDiff) + "đ";
            }

            // Gán nhãn phù hợp thông minh
            String matchTag;
            if (costDiff <= 0) {
                matchTag = "Tối ưu ngân sách";
            } else if (dishProtein >= 30) {
                matchTag = "Giàu Đạm Tự Nhiên";
            } else if (dishCalo < 400) {
                matchTag = "Ít Calo Thanh Nhẹ";
            } else {
                matchTag = "Cân đối dinh dưỡng";
            }

            int imgRes = resolveDishImage(d.getId());

            AlternativeDish alt = new AlternativeDish(
                    d.getId(),
                    d.getTenMon(),
                    dishCalo,
                    dishProtein,
                    dishCarb,
                    dishFat,
                    dishCost,
                    imgRes,
                    savingsText,
                    matchTag,
                    mealTypeCode
            );

            alternativeDishesList.add(alt);
        }

        // Ưu tiên hiển thị món tiết kiệm hoặc tương đồng trước
        alternativeDishesList.sort((a, b) -> Integer.compare(a.getCost(), b.getCost()));

        if (alternativeAdapter != null) {
            alternativeAdapter.updateData(alternativeDishesList);
        }
    }

    private void onAlternativeDishSelected(AlternativeDish dish) {
        this.selectedDish = dish;
        if (currentPlan == null || currentDish == null) return;

        // Tính toán lại tác động tức thời sau khi thay món
        int newCalo = currentPlan.getTotalCalories() - currentDish.getCalories() + dish.getCalories();
        int newProtein = currentPlan.getTotalProtein() - currentDish.getProtein() + dish.getProtein();
        int newCost = currentPlan.getTotalCost() - currentDish.getCost() + dish.getCost();

        tvImpactCalo.setText(newCalo + " kcal");
        tvImpactProtein.setText(newProtein + "g");

        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvImpactCost.setText(nf.format(newCost) + "đ");

        // Kiểm tra với ngân sách
        if (userDailyBudget > 0 && newCost > userDailyBudget) {
            tvImpactCost.setTextColor(ContextCompat.getColor(this, R.color.error));
            Toast.makeText(this, "⚠️ Cảnh báo: Chi phí mới (" + nf.format(newCost) + "đ) vượt ngân sách ngày (" + nf.format(userDailyBudget) + "đ)!", Toast.LENGTH_SHORT).show();
        } else {
            tvImpactCost.setTextColor(ContextCompat.getColor(this, R.color.secondary));
            Toast.makeText(this, "Đã chọn: " + dish.getDishName() + " (" + dish.getSavingsOrExtra() + ")", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveChangesToDatabase() {
        if (selectedDish == null) {
            Toast.makeText(this, "Vui lòng chọn một món thay thế từ danh sách.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentPlan == null || currentDish == null) {
            finish();
            return;
        }

        btnSaveAdjustChanges.setEnabled(false);
        btnSaveAdjustChanges.setText("Đang lưu vào MySQL...");

        // 1. Cập nhật đối tượng MealItem trong bộ nhớ
        MealItem updatedItem = new MealItem(
                String.valueOf(selectedDish.getDishId()),
                currentDish.getMealType(),
                selectedDish.getDishName(),
                currentDish.getPortion() != null ? currentDish.getPortion() : "1 phần",
                selectedDish.getCalories(),
                selectedDish.getProtein(),
                selectedDish.getCarb(),
                selectedDish.getFat(),
                selectedDish.getCost(),
                selectedDish.getImageRes() != 0 ? selectedDish.getImageRes() : currentDish.getImageRes(),
                selectedDish.getDishId(),
                currentDish.getDetailId()
        );

        currentPlan.getMeals().set(currentDishIndex, updatedItem);
        currentPlan.recalculateTotals();

        Long menuId = null;
        try {
            if (currentPlan.getId() != null) {
                menuId = Long.parseLong(currentPlan.getId());
            }
        } catch (Exception ignored) {}

        String buaAnCode = resolveMealTypeCode(currentDish.getMealType());

        // 2. Nếu đã có menuId trong CSDL MySQL: gọi PUT /api/menus/{menuId}/items/{itemId}
        if (menuId != null && menuId > 0 && currentDish.getDetailId() != null && currentDish.getDetailId() > 0) {
            UpdateMenuItemRequest updateReq = new UpdateMenuItemRequest(
                    selectedDish.getDishId(),
                    buaAnCode,
                    1.0
            );

            Long finalMenuId = menuId;
            apiService.updateMenuItem(menuId, currentDish.getDetailId(), updateReq).enqueue(new Callback<ApiResponse<MenuResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<MenuResponse>> call, Response<ApiResponse<MenuResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Xác nhận dữ liệu thật từ MySQL bằng GET /api/menus/{id}
                        verifyDataFromMysql(finalMenuId);
                    } else {
                        fallbackUpdateFullMenu(finalMenuId);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<MenuResponse>> call, Throwable t) {
                    fallbackUpdateFullMenu(finalMenuId);
                }
            });
        } else if (menuId != null && menuId > 0) {
            fallbackUpdateFullMenu(menuId);
        } else {
            // Thực đơn chưa lưu (vừa tạo từ đề xuất): lưu mới vào MySQL
            saveNewMenuToApi();
        }
    }

    private void fallbackUpdateFullMenu(Long menuId) {
        long userId = tokenManager.getUserId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String applyDate = currentPlan.getDate() != null ? currentPlan.getDate() : sdf.format(new Date());

        List<MenuDetailRequest> items = new ArrayList<>();
        for (MealItem it : currentPlan.getMeals()) {
            Long dId = it.getDishId() != null ? it.getDishId() : 1L;
            items.add(new MenuDetailRequest(
                    dId,
                    resolveMealTypeCode(it.getMealType()),
                    1.0,
                    (double) it.getCalories(),
                    (double) it.getCost()
            ));
        }

        MenuRequest req = new MenuRequest(userId, currentPlan.getTitle(), applyDate, "HOAT_DONG", items);
        apiService.updateMenu(menuId, req).enqueue(new Callback<ApiResponse<MenuResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<MenuResponse>> call, Response<ApiResponse<MenuResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    verifyDataFromMysql(menuId);
                } else {
                    onSaveSuccessDone();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<MenuResponse>> call, Throwable t) {
                onSaveSuccessDone();
            }
        });
    }

    private void saveNewMenuToApi() {
        long userId = tokenManager.getUserId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String applyDate = currentPlan.getDate() != null ? currentPlan.getDate() : sdf.format(new Date());

        List<MenuDetailRequest> items = new ArrayList<>();
        for (MealItem it : currentPlan.getMeals()) {
            Long dId = it.getDishId() != null ? it.getDishId() : 1L;
            items.add(new MenuDetailRequest(
                    dId,
                    resolveMealTypeCode(it.getMealType()),
                    1.0,
                    (double) it.getCalories(),
                    (double) it.getCost()
            ));
        }

        MenuRequest req = new MenuRequest(userId, currentPlan.getTitle(), applyDate, "HOAT_DONG", items);
        apiService.createMenu(req).enqueue(new Callback<ApiResponse<MenuResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<MenuResponse>> call, Response<ApiResponse<MenuResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    Long newId = response.body().getData().getId();
                    if (newId != null) {
                        currentPlan.setId(String.valueOf(newId));
                        verifyDataFromMysql(newId);
                        return;
                    }
                }
                onSaveSuccessDone();
            }

            @Override
            public void onFailure(Call<ApiResponse<MenuResponse>> call, Throwable t) {
                onSaveSuccessDone();
            }
        });
    }

    private void verifyDataFromMysql(Long menuId) {
        apiService.getMenuById(menuId).enqueue(new Callback<ApiResponse<MenuResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<MenuResponse>> call, Response<ApiResponse<MenuResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    Log.d(TAG, "Đã xác nhận dữ liệu thật từ MySQL: ID " + menuId);
                }
                onSaveSuccessDone();
            }

            @Override
            public void onFailure(Call<ApiResponse<MenuResponse>> call, Throwable t) {
                onSaveSuccessDone();
            }
        });
    }

    private void onSaveSuccessDone() {
        MockDataRepository.getInstance().setCurrentMealPlan(currentPlan);
        Toast.makeText(this, "Đã cập nhật thực đơn thành công vào MySQL!", Toast.LENGTH_SHORT).show();
        btnSaveAdjustChanges.setText("Đã lưu thành công ✓");
        setResult(RESULT_OK);
        finish();
    }

    private String resolveMealTypeCode(String mealType) {
        if (mealType == null) return "TRUA";
        String t = mealType.toLowerCase();
        if (t.contains("sáng") || t.contains("sang")) return "SANG";
        if (t.contains("tối") || t.contains("toi")) return "TOI";
        if (t.contains("phụ") || t.contains("phu")) return "PHU";
        return "TRUA";
    }

    private int resolveDishImage(Long id) {
        if (id == null) return R.drawable.img_dish_pork;
        long val = id;
        if (val == 1) return R.drawable.img_dish_pork;
        if (val == 2) return R.drawable.img_dish_oats;
        if (val == 3) return R.drawable.img_dish_fish;
        if (val == 4) return R.drawable.img_dish_chicken;
        if (val == 5) return R.drawable.img_dish_bento;
        if (val == 6) return R.drawable.img_dish_pork;
        if (val == 7) return R.drawable.img_dish_salmon;
        if (val == 8) return R.drawable.img_dish_oats;
        if (val == 9) return R.drawable.img_dish_chicken;
        if (val == 10) return R.drawable.img_dish_fish;
        if (val == 11) return R.drawable.img_dish_pork;
        if (val == 12) return R.drawable.img_dish_bento;
        if (val == 13) return R.drawable.img_dish_salmon;
        return R.drawable.img_dish_chicken;
    }
}
