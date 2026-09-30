package com.example.phamnguyenlananh.ui.meal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.adapter.MealDetailSectionAdapter;
import com.example.phamnguyenlananh.data.MockDataRepository;
import com.example.phamnguyenlananh.data.api.ApiClient;
import com.example.phamnguyenlananh.data.api.NutriBudgetApiService;
import com.example.phamnguyenlananh.data.api.TokenManager;
import com.example.phamnguyenlananh.data.api.model.ApiResponse;
import com.example.phamnguyenlananh.data.api.model.MenuDetailRequest;
import com.example.phamnguyenlananh.data.api.model.MenuDetailResponse;
import com.example.phamnguyenlananh.data.api.model.MenuRequest;
import com.example.phamnguyenlananh.data.api.model.MenuResponse;
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

public class MealDetailActivity extends AppCompatActivity {
    private static final String TAG = "MealDetailActivity";

    private ImageView btnBackDetail, btnSharePlan, btnFavoritePlan, imgDetailHero;
    private TextView tvDetailTitle, tvDetailSubtitle, tvDetailCalo, tvDetailProtein, tvDetailCarbs, tvDetailTotalCost;
    private RecyclerView rvDetailMeals;
    private MealDetailSectionAdapter mealsAdapter;
    private Button btnNavCostOverview, btnNavGroceryFromDetail, btnNavAdjustMeal, btnSavePlanToHistory;

    private NutriBudgetApiService apiService;
    private TokenManager tokenManager;
    private MealPlan currentPlan;
    private long menuId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);

        apiService = ApiClient.getNutriBudgetApiService(this);
        tokenManager = TokenManager.getInstance(this);

        menuId = getIntent().getLongExtra("EXTRA_MENU_ID", 0);

        if (getIntent().hasExtra("EXTRA_MEAL_PLAN")) {
            currentPlan = (MealPlan) getIntent().getSerializableExtra("EXTRA_MEAL_PLAN");
            if (currentPlan != null) {
                MockDataRepository.getInstance().setCurrentMealPlan(currentPlan);
                if (menuId <= 0) {
                    try {
                        menuId = Long.parseLong(currentPlan.getId());
                    } catch (Exception ignored) {}
                }
            }
        }
        if (currentPlan == null) {
            currentPlan = MockDataRepository.getInstance().getCurrentMealPlan();
        }

        initViews();
        setupListeners();
        setupRecyclerView();

        if (menuId > 0) {
            loadMenuFromApi(menuId);
        } else {
            updateUI();
        }
    }

    private void initViews() {
        btnBackDetail = findViewById(R.id.btnBackDetail);
        btnSharePlan = findViewById(R.id.btnSharePlan);
        btnFavoritePlan = findViewById(R.id.btnFavoritePlan);
        imgDetailHero = findViewById(R.id.imgDetailHero);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailSubtitle = findViewById(R.id.tvDetailSubtitle);
        tvDetailCalo = findViewById(R.id.tvDetailCalo);
        tvDetailProtein = findViewById(R.id.tvDetailProtein);
        tvDetailCarbs = findViewById(R.id.tvDetailCarbs);
        tvDetailTotalCost = findViewById(R.id.tvDetailTotalCost);
        rvDetailMeals = findViewById(R.id.rvDetailMeals);

        btnNavCostOverview = findViewById(R.id.btnNavCostOverview);
        btnNavGroceryFromDetail = findViewById(R.id.btnNavGroceryFromDetail);
        btnNavAdjustMeal = findViewById(R.id.btnNavAdjustMeal);
        btnSavePlanToHistory = findViewById(R.id.btnSavePlanToHistory);
    }

    private void setupListeners() {
        btnBackDetail.setOnClickListener(v -> finish());
        btnSharePlan.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String title = currentPlan != null ? currentPlan.getTitle() : "Thá»±c Ä‘Æ¡n NutriBudget";
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Xem thá»±c Ä‘Æ¡n dinh dÆ°á»¡ng chuáº©n ngÃ¢n sÃ¡ch cá»§a tÃ´i trÃªn NutriBudget: " + title);
            startActivity(Intent.createChooser(shareIntent, "Chia sáº» thá»±c Ä‘Æ¡n"));
        });

        btnFavoritePlan.setOnClickListener(v -> Toast.makeText(this, "ÄÃ£ lÆ°u thá»±c Ä‘Æ¡n vÃ o danh sÃ¡ch yÃªu thÃ­ch! â­", Toast.LENGTH_SHORT).show());

        btnNavCostOverview.setOnClickListener(v -> {
            Intent intent = new Intent(this, CostOverviewActivity.class);
            if (currentPlan != null) {
                intent.putExtra("EXTRA_MEAL_PLAN", currentPlan);
                if (menuId > 0) {
                    intent.putExtra("EXTRA_MENU_ID", menuId);
                }
            } else if (menuId > 0) {
                intent.putExtra("EXTRA_MENU_ID", menuId);
            }
            startActivity(intent);
        });

        btnNavGroceryFromDetail.setOnClickListener(v -> {
            Intent intent = new Intent(this, GroceryListActivity.class);
            if (menuId > 0) {
                intent.putExtra("EXTRA_MENU_ID", menuId);
            }
            if (currentPlan != null) {
                intent.putExtra("EXTRA_MEAL_PLAN", currentPlan);
            }
            startActivity(intent);
        });

        btnNavAdjustMeal.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdjustMealActivity.class);
            if (currentPlan != null) {
                intent.putExtra("EXTRA_MEAL_PLAN", currentPlan);
            }
            startActivity(intent);
        });

        btnSavePlanToHistory.setOnClickListener(v -> saveCurrentMealPlanToApi());
    }

    private void loadMenuFromApi(long id) {
        apiService.getMenuById(id).enqueue(new Callback<ApiResponse<MenuResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<MenuResponse>> call, Response<ApiResponse<MenuResponse>> response) {
                if (response.code() == 401 || response.code() == 403) {
                    Toast.makeText(MealDetailActivity.this, "PhiÃªn Ä‘Äƒng nháº­p háº¿t háº¡n.", Toast.LENGTH_SHORT).show();
                    tokenManager.clearSession();
                    Intent loginIntent = new Intent(MealDetailActivity.this, LoginActivity.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                    finish();
                    return;
                }

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    MenuResponse menu = response.body().getData();
                    bindMenuResponse(menu);
                } else {
                    updateUI();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<MenuResponse>> call, Throwable t) {
                Log.e(TAG, "loadMenuFromApi failed: " + t.getMessage());
                updateUI();
            }
        });
    }

    private void bindMenuResponse(MenuResponse menu) {
        tvDetailTitle.setText(menu.getTenThucDon());
        if (menu.getNgayApDung() != null && !menu.getNgayApDung().isEmpty()) {
            tvDetailSubtitle.setText("NgÃ y Ã¡p dá»¥ng: " + menu.getNgayApDung());
        }

        double calo = menu.getTongCalo() != null ? menu.getTongCalo() : 0;
        double protein = menu.getTongProtein() != null ? menu.getTongProtein() : 0;
        double carbs = menu.getTongCarb() != null ? menu.getTongCarb() : 0;
        double cost = menu.getTongChiPhi() != null ? menu.getTongChiPhi() : 0;

        tvDetailCalo.setText(String.format(Locale.getDefault(), "%,.0f kcal", calo));
        tvDetailProtein.setText(String.format(Locale.getDefault(), "%.0fg", protein));
        tvDetailCarbs.setText(String.format(Locale.getDefault(), "%.0fg", carbs));

        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvDetailTotalCost.setText(currencyFormat.format(cost) + "Ä‘");

        List<MealItem> items = new ArrayList<>();
        if (menu.getChiTietThucDon() != null) {
            for (MenuDetailResponse d : menu.getChiTietThucDon()) {
                String mealType = "Bá»¯a sÃ¡ng";
                if (d.getBuaAn() != null) {
                    String bua = d.getBuaAn().toUpperCase();
                    if (bua.contains("TRUA")) mealType = "Bá»¯a trÆ°a";
                    else if (bua.contains("TOI")) mealType = "Bá»¯a tá»‘i";
                    else if (bua.contains("PHU")) mealType = "Bá»¯a phá»¥";
                }

                int imgRes = R.drawable.img_dish_bento;
                if (mealType.equals("Bá»¯a sÃ¡ng")) imgRes = R.drawable.img_dish_oats;
                else if (mealType.equals("Bá»¯a trÆ°a")) imgRes = R.drawable.img_dish_chicken;
                else if (mealType.equals("Bá»¯a tá»‘i")) imgRes = R.drawable.img_dish_fish;

                MealItem item = new MealItem(
                        String.valueOf(d.getId()),
                        mealType,
                        d.getTenMon(),
                        d.getKhauPhan() != null ? d.getKhauPhan() : "1 pháº§n",
                        d.getCalo() != null ? d.getCalo().intValue() : 0,
                        d.getProtein() != null ? d.getProtein().intValue() : 0,
                        d.getCarb() != null ? d.getCarb().intValue() : 0,
                        d.getFat() != null ? d.getFat().intValue() : 0,
                        d.getChiPhi() != null ? d.getChiPhi().intValue() : 0,
                        imgRes,
                        d.getMonAnId(),
                        d.getId()
                );
                items.add(item);
            }
        }

        if (mealsAdapter != null) {
            mealsAdapter.updateData(items);
        } else {
            mealsAdapter = new MealDetailSectionAdapter(items);
            rvDetailMeals.setAdapter(mealsAdapter);
        }

        currentPlan = new MealPlan(
                String.valueOf(menu.getId()),
                menu.getTenThucDon(),
                "NgÃ y Ã¡p dá»¥ng: " + menu.getNgayApDung(),
                95,
                15000,
                (int) calo,
                (int) protein,
                (int) carbs,
                menu.getTongFat() != null ? menu.getTongFat().intValue() : 0,
                (int) cost,
                items.size(),
                menu.getTrangThai(),
                menu.getNgayApDung(),
                R.drawable.img_dish_vietnamese_lunch,
                items
        );
        MockDataRepository.getInstance().setCurrentMealPlan(currentPlan);
    }

    private void saveCurrentMealPlanToApi() {
        if (currentPlan == null) return;

        long userId = tokenManager.getUserId();

        btnSavePlanToHistory.setEnabled(false);
        btnSavePlanToHistory.setText("Äang lÆ°u vÃ o MySQL...");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String applyDate = currentPlan.getDate() != null ? currentPlan.getDate() : sdf.format(new Date());

        List<MenuDetailRequest> items = new ArrayList<>();
        if (currentPlan.getMeals() != null) {
            for (MealItem item : currentPlan.getMeals()) {
                Long dishId = item.getDishId();
                if (dishId == null) {
                    try {
                        dishId = Long.parseLong(item.getId());
                    } catch (Exception e) {
                        dishId = 1L;
                    }
                }

                // Use raw buaAnCode (reliable), fallback to ASCII keyword parsing
                String mealTypeCode = "TRUA";
                if (item.getBuaAnCode() != null && !item.getBuaAnCode().isEmpty()) {
                    mealTypeCode = item.getBuaAnCode().toUpperCase();
                } else {
                    String t = item.getMealType() != null ? item.getMealType().toLowerCase() : "";
                                    if (t.contains("sang")) mealTypeCode = "SANG";
                    else if (t.contains("toi")) mealTypeCode = "TOI";
                    else if (t.contains("phu")) mealTypeCode = "PHU";
                }

                items.add(new MenuDetailRequest(
                        dishId,
                        mealTypeCode,
                        1.0,
                        (double) item.getCalories(),
                        (double) item.getCost()
                ));
            }
        }

        MenuRequest request = new MenuRequest(
                userId,
                currentPlan.getTitle(),
                applyDate,
                "HOAT_DONG",
                items
        );

        apiService.createMenu(request).enqueue(new Callback<ApiResponse<MenuResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<MenuResponse>> call, Response<ApiResponse<MenuResponse>> response) {
                if (response.code() == 401 || response.code() == 403) {
                    Toast.makeText(MealDetailActivity.this, "PhiÃªn Ä‘Äƒng nháº­p háº¿t háº¡n.", Toast.LENGTH_SHORT).show();
                    tokenManager.clearSession();
                    startActivity(new Intent(MealDetailActivity.this, LoginActivity.class));
                    finish();
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    MockDataRepository.getInstance().saveCurrentPlanToHistory();
                    Toast.makeText(MealDetailActivity.this, "ÄÃ£ lÆ°u thá»±c Ä‘Æ¡n vÃ o CSDL MySQL thÃ nh cÃ´ng!", Toast.LENGTH_SHORT).show();
                    btnSavePlanToHistory.setText("ÄÃ£ lÆ°u thá»±c Ä‘Æ¡n âœ“");
                    new Handler(Looper.getMainLooper()).postDelayed(() -> finish(), 1000);
                } else {
                    btnSavePlanToHistory.setEnabled(true);
                    btnSavePlanToHistory.setText("LÆ°u thá»±c Ä‘Æ¡n nÃ y");
                    String msg = response.body() != null ? response.body().getMessage() : "Lá»—i lÆ°u thá»±c Ä‘Æ¡n";
                    Toast.makeText(MealDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<MenuResponse>> call, Throwable t) {
                btnSavePlanToHistory.setEnabled(true);
                btnSavePlanToHistory.setText("LÆ°u thá»±c Ä‘Æ¡n nÃ y");
                Log.e(TAG, "Lá»—i káº¿t ná»‘i lÆ°u thá»±c Ä‘Æ¡n", t);
                Toast.makeText(MealDetailActivity.this, "Lá»—i káº¿t ná»‘i mÃ¡y chá»§. Vui lÃ²ng thá»­ láº¡i!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        rvDetailMeals.setLayoutManager(new LinearLayoutManager(this));
        if (currentPlan != null && currentPlan.getMeals() != null) {
            mealsAdapter = new MealDetailSectionAdapter(currentPlan.getMeals());
            rvDetailMeals.setAdapter(mealsAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (menuId > 0) {
            loadMenuFromApi(menuId);
        } else {
            MealPlan p = MockDataRepository.getInstance().getCurrentMealPlan();
            if (p != null) {
                currentPlan = p;
            }
            updateUI();
        }
    }

    private void updateUI() {
        if (currentPlan == null) return;

        tvDetailTitle.setText(currentPlan.getTitle());
        tvDetailSubtitle.setText(currentPlan.getSubtitle());
        tvDetailCalo.setText(String.format(Locale.getDefault(), "%,d kcal", currentPlan.getTotalCalories()));
        tvDetailProtein.setText(currentPlan.getTotalProtein() + "g");
        tvDetailCarbs.setText(currentPlan.getTotalCarbs() + "g");

        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvDetailTotalCost.setText(currencyFormat.format(currentPlan.getTotalCost()) + "Ä‘");

        if (currentPlan.getImageRes() != 0) {
            imgDetailHero.setImageResource(currentPlan.getImageRes());
        }

        if (mealsAdapter != null && currentPlan.getMeals() != null) {
            mealsAdapter.updateData(currentPlan.getMeals());
        }
    }
}