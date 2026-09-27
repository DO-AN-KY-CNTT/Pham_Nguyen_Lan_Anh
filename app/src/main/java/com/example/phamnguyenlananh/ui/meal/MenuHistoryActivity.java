package com.example.phamnguyenlananh.ui.meal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.adapter.MealHistoryAdapter;
import com.example.phamnguyenlananh.data.api.ApiClient;
import com.example.phamnguyenlananh.data.api.NutriBudgetApiService;
import com.example.phamnguyenlananh.data.api.TokenManager;
import com.example.phamnguyenlananh.data.api.model.ApiResponse;
import com.example.phamnguyenlananh.data.api.model.MenuHistoryResponse;
import com.example.phamnguyenlananh.ui.auth.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuHistoryActivity extends AppCompatActivity {
    private static final String TAG = "MenuHistoryActivity";

    private RecyclerView rvMealHistory;
    private MealHistoryAdapter historyAdapter;
    private ImageView btnSearchHistory;
    private ProgressBar progressHistoryLoading;
    private TextView tvEmptyHistory;
    private TextView tvHistoryBannerTitle, tvHistoryBannerSubtitle;

    private NutriBudgetApiService apiService;
    private TokenManager tokenManager;
    private List<MenuHistoryResponse> fullHistoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_history);

        apiService = ApiClient.getNutriBudgetApiService(this);
        tokenManager = TokenManager.getInstance(this);

        initViews();
        setupRecyclerView();
        loadHistoryData();
    }

    private void initViews() {
        rvMealHistory = findViewById(R.id.rvMealHistory);
        btnSearchHistory = findViewById(R.id.btnSearchHistory);
        progressHistoryLoading = findViewById(R.id.progressHistoryLoading);
        tvEmptyHistory = findViewById(R.id.tvEmptyHistory);
        tvHistoryBannerTitle = findViewById(R.id.tvHistoryBannerTitle);
        tvHistoryBannerSubtitle = findViewById(R.id.tvHistoryBannerSubtitle);

        if (btnSearchHistory != null) {
            btnSearchHistory.setOnClickListener(v -> finish());
        }
    }

    private void setupRecyclerView() {
        rvMealHistory.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new MealHistoryAdapter(new ArrayList<>(), item -> {
            if (item != null && item.getThucDonId() != null && item.getThucDonId() > 0) {
                Intent intent = new Intent(this, MealDetailActivity.class);
                intent.putExtra("EXTRA_MENU_ID", item.getThucDonId().longValue());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Lịch sử này không có chi tiết thực đơn", Toast.LENGTH_SHORT).show();
            }
        });
        rvMealHistory.setAdapter(historyAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistoryData();
    }

    private void loadHistoryData() {
        long userId = tokenManager.getUserId();
        if (userId <= 0) {
            redirectToLogin("Vui lòng đăng nhập lại");
            return;
        }

        if (progressHistoryLoading != null) progressHistoryLoading.setVisibility(View.VISIBLE);

        apiService.getMenuHistory(userId).enqueue(new Callback<ApiResponse<List<MenuHistoryResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<MenuHistoryResponse>>> call, Response<ApiResponse<List<MenuHistoryResponse>>> response) {
                if (progressHistoryLoading != null) progressHistoryLoading.setVisibility(View.GONE);

                if (response.code() == 401 || response.code() == 403) {
                    redirectToLogin("Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.");
                    return;
                }

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<MenuHistoryResponse> list = response.body().getData();
                    fullHistoryList = list;

                    if (list.isEmpty()) {
                        rvMealHistory.setVisibility(View.GONE);
                        tvEmptyHistory.setVisibility(View.VISIBLE);
                        if (tvHistoryBannerSubtitle != null) {
                            tvHistoryBannerSubtitle.setText("Chưa ghi nhận thực đơn nào");
                        }
                    } else {
                        rvMealHistory.setVisibility(View.VISIBLE);
                        tvEmptyHistory.setVisibility(View.GONE);
                        if (tvHistoryBannerSubtitle != null) {
                            tvHistoryBannerSubtitle.setText("Đã ghi nhận " + list.size() + " thao tác thực đơn");
                        }
                        historyAdapter.updateData(list);
                    }
                } else {
                    if (fullHistoryList.isEmpty()) {
                        tvEmptyHistory.setVisibility(View.VISIBLE);
                        rvMealHistory.setVisibility(View.GONE);
                    }
                    Toast.makeText(MenuHistoryActivity.this, "Không thể tải lịch sử thực đơn từ hệ thống", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<MenuHistoryResponse>>> call, Throwable t) {
                if (progressHistoryLoading != null) progressHistoryLoading.setVisibility(View.GONE);
                Log.e(TAG, "loadHistoryData error: " + t.getMessage());
                if (fullHistoryList.isEmpty()) {
                    tvEmptyHistory.setVisibility(View.VISIBLE);
                    rvMealHistory.setVisibility(View.GONE);
                }
                Toast.makeText(MenuHistoryActivity.this, "Lỗi kết nối khi tải lịch sử thực đơn", Toast.LENGTH_SHORT).show();
            }
        });
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
