package com.example.phamnguyenlananh.ui.meal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.adapter.GroceryListAdapter;
import com.example.phamnguyenlananh.data.api.ApiClient;
import com.example.phamnguyenlananh.data.api.NutriBudgetApiService;
import com.example.phamnguyenlananh.data.api.TokenManager;
import com.example.phamnguyenlananh.data.api.model.ApiResponse;
import com.example.phamnguyenlananh.data.api.model.IngredientItemResponse;
import com.example.phamnguyenlananh.data.api.model.MenuIngredientsResponse;
import com.example.phamnguyenlananh.data.api.model.MenuResponse;
import com.example.phamnguyenlananh.model.MealPlan;
import com.example.phamnguyenlananh.ui.auth.LoginActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroceryListActivity extends AppCompatActivity {
    private static final String TAG = "GroceryListActivity";

    private ImageView btnBackGrocery, btnAddGroceryItem;
    private TextView tvGrocerySubtitle, tvGroceryTotalEstimate, tvGroceryProgressText, tvGroceryBottomTotal, tvEmptyGrocery;
    private ProgressBar progressGroceryLoading;
    private LinearProgressIndicator progressGrocery;
    private RecyclerView rvGroceryList;
    private Button btnFinishShopping;

    private GroceryListAdapter adapter;
    private NutriBudgetApiService apiService;
    private TokenManager tokenManager;

    private long menuId = -1;
    private MealPlan currentPlan;
    private MenuIngredientsResponse currentIngredientsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        apiService = ApiClient.getNutriBudgetApiService(this);
        tokenManager = TokenManager.getInstance(this);

        initViews();
        setupListeners();
        setupRecyclerView();

        resolveMenuIdAndLoadData();
    }

    private void initViews() {
        btnBackGrocery = findViewById(R.id.btnBackGrocery);
        btnAddGroceryItem = findViewById(R.id.btnAddGroceryItem);
        tvGrocerySubtitle = findViewById(R.id.tvGrocerySubtitle);
        tvGroceryTotalEstimate = findViewById(R.id.tvGroceryTotalEstimate);
        tvGroceryProgressText = findViewById(R.id.tvGroceryProgressText);
        tvGroceryBottomTotal = findViewById(R.id.tvGroceryBottomTotal);
        tvEmptyGrocery = findViewById(R.id.tvEmptyGrocery);
        progressGroceryLoading = findViewById(R.id.progressGroceryLoading);
        progressGrocery = findViewById(R.id.progressGrocery);
        rvGroceryList = findViewById(R.id.rvGroceryList);
        btnFinishShopping = findViewById(R.id.btnFinishShopping);
    }

    private void setupRecyclerView() {
        rvGroceryList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroceryListAdapter(new ArrayList<>(), (item, position, isChecked) -> {
            updateProgressSummary();
        });
        rvGroceryList.setAdapter(adapter);
    }

    private void setupListeners() {
        // Quay lai Chi tiet thuc don
        btnBackGrocery.setOnClickListener(v -> finish());

        // Dieu chinh thuc don
        btnAddGroceryItem.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdjustMealActivity.class);
            if (currentPlan != null) {
                intent.putExtra("EXTRA_MEAL_PLAN", currentPlan);
            }
            if (menuId > 0) {
                intent.putExtra("EXTRA_MENU_ID", menuId);
            }
            startActivity(intent);
        });

        btnFinishShopping.setOnClickListener(v -> {
            Toast.makeText(this, "Hoàn tất mua sắm cho thực đơn này! 🎉", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (menuId > 0) {
            loadIngredientsData(menuId);
        }
    }

    private void resolveMenuIdAndLoadData() {
        if (getIntent().hasExtra("EXTRA_MENU_ID")) {
            menuId = getIntent().getLongExtra("EXTRA_MENU_ID", -1);
        }

        if (getIntent().hasExtra("EXTRA_MEAL_PLAN")) {
            currentPlan = (MealPlan) getIntent().getSerializableExtra("EXTRA_MEAL_PLAN");
            if (menuId <= 0 && currentPlan != null && currentPlan.getId() != null) {
                try {
                    menuId = Long.parseLong(currentPlan.getId());
                } catch (Exception ignored) {}
            }
        }

        if (menuId > 0) {
            loadIngredientsData(menuId);
        } else {
            // Neu khong co menuId duoc truyen vao (VD: mo truc tiep tu Home), lay thuc don moi nhat cua nguoi dung
            loadLatestUserMenu();
        }
    }

    private void loadLatestUserMenu() {
        long userId = tokenManager.getUserId();
        if (userId <= 0) {
            redirectToLogin("Vui lòng đăng nhập lại");
            return;
        }

        if (progressGroceryLoading != null) progressGroceryLoading.setVisibility(View.VISIBLE);

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
                        loadIngredientsData(menuId);
                    } else {
                        if (progressGroceryLoading != null) progressGroceryLoading.setVisibility(View.GONE);
                        showEmptyState("Chưa có thực đơn nào để tạo danh sách nguyên liệu");
                    }
                } else {
                    if (progressGroceryLoading != null) progressGroceryLoading.setVisibility(View.GONE);
                    showEmptyState("Không thể tải danh sách thực đơn");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<MenuResponse>>> call, Throwable t) {
                if (progressGroceryLoading != null) progressGroceryLoading.setVisibility(View.GONE);
                showEmptyState("Lỗi kết nối khi tải thực đơn");
            }
        });
    }

    private void loadIngredientsData(long mId) {
        if (!tokenManager.isLoggedIn()) {
            redirectToLogin("Vui lòng đăng nhập lại");
            return;
        }

        if (progressGroceryLoading != null) progressGroceryLoading.setVisibility(View.VISIBLE);

        apiService.getMenuIngredients(mId).enqueue(new Callback<ApiResponse<MenuIngredientsResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<MenuIngredientsResponse>> call, Response<ApiResponse<MenuIngredientsResponse>> response) {
                if (progressGroceryLoading != null) progressGroceryLoading.setVisibility(View.GONE);

                if (response.code() == 401 || response.code() == 403) {
                    redirectToLogin("Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.");
                    return;
                }

                if (response.code() == 404) {
                    showEmptyState("Thực đơn không tồn tại hoặc đã bị xóa");
                    Toast.makeText(GroceryListActivity.this, "Thực đơn không tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    currentIngredientsData = response.body().getData();
                    displayIngredients(currentIngredientsData);
                } else {
                    showEmptyState("Chưa có dữ liệu nguyên liệu");
                    Toast.makeText(GroceryListActivity.this, "Không thể tải nguyên liệu thực đơn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<MenuIngredientsResponse>> call, Throwable t) {
                if (progressGroceryLoading != null) progressGroceryLoading.setVisibility(View.GONE);
                Log.e(TAG, "loadIngredientsData onFailure: " + t.getMessage());
                showEmptyState("Lỗi kết nối máy chủ");
                Toast.makeText(GroceryListActivity.this, "Lỗi kết nối khi tải nguyên liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayIngredients(MenuIngredientsResponse data) {
        if (data.getTenThucDon() != null && !data.getTenThucDon().trim().isEmpty()) {
            if (tvGrocerySubtitle != null) {
                tvGrocerySubtitle.setText("Thực đơn: " + data.getTenThucDon());
            }
        }

        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        BigDecimal totalCost = data.getTongChiPhiNguyenLieu();
        String totalCostStr = nf.format(totalCost) + "đ";

        tvGroceryTotalEstimate.setText(totalCostStr);
        tvGroceryBottomTotal.setText(totalCostStr);

        List<IngredientItemResponse> items = data.getNguyenLieu();
        if (items == null || items.isEmpty()) {
            showEmptyState("Chưa có dữ liệu nguyên liệu");
        } else {
            if (tvEmptyGrocery != null) tvEmptyGrocery.setVisibility(View.GONE);
            rvGroceryList.setVisibility(View.VISIBLE);
            adapter.updateData(items);
            updateProgressSummary();
        }
    }

    private void updateProgressSummary() {
        if (adapter == null) return;
        List<IngredientItemResponse> list = adapter.getIngredients();
        int total = list.size();
        int checked = 0;
        for (IngredientItemResponse item : list) {
            if (item.isChecked()) checked++;
        }

        int percent = total > 0 ? (int) Math.round(((double) checked / total) * 100) : 0;
        tvGroceryProgressText.setText(checked + " / " + total + " món (" + percent + "%)");
        progressGrocery.setProgress(percent);
    }

    private void showEmptyState(String message) {
        if (tvEmptyGrocery != null) {
            tvEmptyGrocery.setText(message);
            tvEmptyGrocery.setVisibility(View.VISIBLE);
        }
        rvGroceryList.setVisibility(View.GONE);
        tvGroceryTotalEstimate.setText("0đ");
        tvGroceryBottomTotal.setText("0đ");
        tvGroceryProgressText.setText("0 / 0 món (0%)");
        progressGrocery.setProgress(0);
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
