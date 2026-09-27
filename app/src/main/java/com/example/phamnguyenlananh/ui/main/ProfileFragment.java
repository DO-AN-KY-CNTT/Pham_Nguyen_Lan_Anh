package com.example.phamnguyenlananh.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.data.api.ApiClient;
import com.example.phamnguyenlananh.data.api.TokenManager;
import com.example.phamnguyenlananh.data.api.model.ApiResponse;
import com.example.phamnguyenlananh.data.api.model.BudgetResponse;
import com.example.phamnguyenlananh.data.api.model.NutritionGoalResponse;
import com.example.phamnguyenlananh.data.api.model.UserResponse;
import com.example.phamnguyenlananh.ui.auth.LoginActivity;
import com.example.phamnguyenlananh.ui.profile.BudgetSettingsActivity;
import com.example.phamnguyenlananh.ui.profile.NutritionGoalsActivity;
import com.example.phamnguyenlananh.ui.profile.ProfileDetailActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private TextView tvProfileName, tvProfileEmail, tvProfileMembership, tvStreakDays, tvSavedMoney;
    private TextView tvGenderAge, tvHeightWeight, tvBmi, tvProfileGoalDesc, tvProfileBudgetDesc;
    private Button btnEditProfileTop, btnEditProfileDialog, btnLogout;
    private View btnNavNutritionGoal, btnNavBudget;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(root);
        setupListeners();
        return root;
    }

    private void initViews(View root) {
        tvProfileName = root.findViewById(R.id.tvProfileName);
        tvProfileEmail = root.findViewById(R.id.tvProfileEmail);
        tvProfileMembership = root.findViewById(R.id.tvProfileMembership);
        tvStreakDays = root.findViewById(R.id.tvStreakDays);
        tvSavedMoney = root.findViewById(R.id.tvSavedMoney);
        tvGenderAge = root.findViewById(R.id.tvGenderAge);
        tvHeightWeight = root.findViewById(R.id.tvHeightWeight);
        tvBmi = root.findViewById(R.id.tvBmi);
        tvProfileGoalDesc = root.findViewById(R.id.tvProfileGoalDesc);
        tvProfileBudgetDesc = root.findViewById(R.id.tvProfileBudgetDesc);
        btnEditProfileTop = root.findViewById(R.id.btnEditProfileTop);
        btnEditProfileDialog = root.findViewById(R.id.btnEditProfileDialog);
        btnLogout = root.findViewById(R.id.btnLogout);
        btnNavNutritionGoal = root.findViewById(R.id.btnNavNutritionGoal);
        btnNavBudget = root.findViewById(R.id.btnNavBudget);
    }

    private void setupListeners() {
        View.OnClickListener openEditProfile = v -> {
            Intent intent = new Intent(getActivity(), ProfileDetailActivity.class);
            startActivity(intent);
        };
        btnEditProfileTop.setOnClickListener(openEditProfile);
        btnEditProfileDialog.setOnClickListener(openEditProfile);

        btnNavNutritionGoal.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NutritionGoalsActivity.class);
            startActivity(intent);
        });

        btnNavBudget.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BudgetSettingsActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            logoutUser("Đã đăng xuất tài khoản");
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfileFromApi();
        loadNutritionGoalFromApi();
        loadBudgetFromApi();
    }

    private void loadProfileFromApi() {
        if (getContext() == null) return;

        SharedPreferences prefs = getContext().getSharedPreferences("NutriBudgetPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt_token", null);

        if (token == null || token.isEmpty()) {
            logoutUser("Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.");
            return;
        }

        ApiClient.getApiService().getProfile("Bearer " + token).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (getContext() == null) return;

                if (response.code() == 401 || response.code() == 403 || (response.code() == 400 && isAuthError(response))) {
                    logoutUser("Phiên đăng nhập không hợp lệ hoặc đã hết hạn.");
                    return;
                }

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess() && response.body().getData() != null) {
                    UserResponse user = response.body().getData();
                    updateUIWithUserData(user);
                } else {
                    String msg = "Không thể tải thông tin người dùng từ máy chủ";
                    if (response.body() != null && response.body().getMessage() != null) {
                        msg = response.body().getMessage();
                    }
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                if (getContext() == null) return;
                Toast.makeText(getContext(), "Không thể kết nối đến máy chủ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNutritionGoalFromApi() {
        if (getContext() == null) return;
        TokenManager tm = TokenManager.getInstance(getContext());
        long userId = tm.getUserId();
        if (userId <= 0) return;

        ApiClient.getNutriBudgetApiService(getContext()).getNutritionGoals(userId).enqueue(new Callback<ApiResponse<List<NutritionGoalResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<NutritionGoalResponse>>> call, Response<ApiResponse<List<NutritionGoalResponse>>> response) {
                if (getContext() == null) return;
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    NutritionGoalResponse goal = response.body().getData().get(0);
                    String goalType = goal.getMucTieu() != null ? goal.getMucTieu() : "Mục tiêu sức khỏe";
                    int calo = goal.getCaloMucTieu() != null ? goal.getCaloMucTieu().intValue() : 0;
                    if (calo > 0) {
                        tvProfileGoalDesc.setText(String.format(java.util.Locale.US, "%s • %,d kcal/ngày", goalType, calo));
                    } else {
                        tvProfileGoalDesc.setText(goalType);
                    }
                } else {
                    tvProfileGoalDesc.setText("Chưa thiết lập");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<NutritionGoalResponse>>> call, Throwable t) {
                // Keep default display
            }
        });
    }

    private void loadBudgetFromApi() {
        if (getContext() == null) return;
        TokenManager tm = TokenManager.getInstance(getContext());
        long userId = tm.getUserId();
        if (userId <= 0) return;

        ApiClient.getNutriBudgetApiService(getContext()).getBudgets(userId).enqueue(new Callback<ApiResponse<List<BudgetResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BudgetResponse>>> call, Response<ApiResponse<List<BudgetResponse>>> response) {
                if (getContext() == null) return;
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    BudgetResponse b = response.body().getData().get(0);
                    NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
                    long daily = b.getNganSachNgay() != null ? b.getNganSachNgay().longValue() : 0;
                    long weekly = b.getNganSachTuan() != null ? b.getNganSachTuan().longValue() : daily * 7;
                    if (daily > 0) {
                        tvProfileBudgetDesc.setText(String.format(Locale.getDefault(), "%s đ/ngày (%s đ/tuần)", nf.format(daily), nf.format(weekly)));
                    }
                } else {
                    tvProfileBudgetDesc.setText("Chưa thiết lập");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<BudgetResponse>>> call, Throwable t) {
                // Keep default display
            }
        });
    }

    private boolean isAuthError(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                String err = response.errorBody().string();
                return err.contains("Chua dang nhap");
            }
        } catch (Exception ignored) {}
        return false;
    }

    private void updateUIWithUserData(UserResponse user) {
        tvProfileName.setText(user.getHoTen() != null ? user.getHoTen() : "Người dùng");
        tvProfileEmail.setText(user.getEmail() != null ? user.getEmail() : "");
        tvProfileMembership.setText("Thành viên NutriBudget");
        tvStreakDays.setText("🔥 7 ngày");
        tvSavedMoney.setText("💰 450.000đ");

        String gender = user.getGioiTinh() != null ? user.getGioiTinh() : "Nữ";
        String dob = user.getNgaySinh() != null ? user.getNgaySinh() : "";
        if (!dob.isEmpty()) {
            tvGenderAge.setText(gender + " • " + dob);
        } else {
            tvGenderAge.setText(gender);
        }

        double height = user.getChieuCao() != null ? user.getChieuCao() : 0.0;
        double weight = user.getCanNang() != null ? user.getCanNang() : 0.0;
        tvHeightWeight.setText(String.format(java.util.Locale.US, "%.1f cm • %.1f kg", height, weight));

        double bmi = user.getBmi() != null ? user.getBmi() : 0.0;
        String bmiCategory;
        if (bmi <= 0) bmiCategory = "Chưa có";
        else if (bmi < 18.5) bmiCategory = "Gầy";
        else if (bmi < 23.0) bmiCategory = "Chuẩn";
        else if (bmi < 25.0) bmiCategory = "Thừa cân";
        else bmiCategory = "Béo phì";

        tvBmi.setText(String.format(java.util.Locale.US, "%.1f (%s)", bmi, bmiCategory));
    }

    private void logoutUser(String message) {
        if (getContext() == null) return;
        SharedPreferences prefs = getContext().getSharedPreferences("NutriBudgetPrefs", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
        TokenManager.getInstance(getContext()).clearSession();

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}