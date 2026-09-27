package com.example.phamnguyenlananh.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.data.api.ApiClient;
import com.example.phamnguyenlananh.data.api.TokenManager;
import com.example.phamnguyenlananh.data.api.model.ApiResponse;
import com.example.phamnguyenlananh.data.api.model.UpdateUserRequest;
import com.example.phamnguyenlananh.data.api.model.UserResponse;
import com.example.phamnguyenlananh.ui.auth.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDetailActivity extends AppCompatActivity {
    private ImageView btnBackProfileDetail;
    private TextView tvEditNameBadge;
    private EditText etProfileFullName, etProfileEmail, etProfileGender, etProfileAge, etProfileHeight, etProfileWeight;
    private Button btnSaveProfile;

    private Long currentUserId = null;
    private String currentPhone = null;
    private String currentBirthDate = null;
    private String currentToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        initViews();
        setupListeners();
        loadProfileData();
    }

    private void initViews() {
        btnBackProfileDetail = findViewById(R.id.btnBackProfileDetail);
        tvEditNameBadge = findViewById(R.id.tvEditNameBadge);
        etProfileFullName = findViewById(R.id.etProfileFullName);
        etProfileEmail = findViewById(R.id.etProfileEmail);
        etProfileGender = findViewById(R.id.etProfileGender);
        etProfileAge = findViewById(R.id.etProfileAge);
        etProfileHeight = findViewById(R.id.etProfileHeight);
        etProfileWeight = findViewById(R.id.etProfileWeight);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        etProfileEmail.setEnabled(false);
    }

    private void loadProfileData() {
        SharedPreferences prefs = getSharedPreferences("NutriBudgetPrefs", Context.MODE_PRIVATE);
        currentToken = prefs.getString("jwt_token", null);

        if (currentToken == null || currentToken.isEmpty()) {
            redirectToLogin("Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.");
            return;
        }

        btnSaveProfile.setEnabled(false);
        ApiClient.getApiService().getProfile("Bearer " + currentToken).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                btnSaveProfile.setEnabled(true);

                if (response.code() == 401 || response.code() == 403 || (response.code() == 400 && isAuthError(response))) {
                    redirectToLogin("Phiên đăng nhập không hợp lệ hoặc đã hết hạn.");
                    return;
                }

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess() && response.body().getData() != null) {
                    UserResponse user = response.body().getData();
                    bindUserData(user);
                } else {
                    String msg = "Không thể tải thông tin cá nhân";
                    if (response.body() != null && response.body().getMessage() != null) {
                        msg = response.body().getMessage();
                    }
                    Toast.makeText(ProfileDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                btnSaveProfile.setEnabled(true);
                Toast.makeText(ProfileDetailActivity.this, "Lỗi kết nối máy chủ: " + t.getMessage(), Toast.LENGTH_LONG).show();
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

    private void bindUserData(UserResponse user) {
        currentUserId = user.getId();
        currentPhone = user.getSoDienThoai();
        currentBirthDate = user.getNgaySinh();

        String name = user.getHoTen() != null ? user.getHoTen() : "";
        tvEditNameBadge.setText(name);
        etProfileFullName.setText(name);
        etProfileEmail.setText(user.getEmail() != null ? user.getEmail() : "");
        etProfileGender.setText(user.getGioiTinh() != null ? user.getGioiTinh() : "Nữ");
        etProfileAge.setText(user.getNgaySinh() != null ? user.getNgaySinh() : "");
        etProfileHeight.setText(user.getChieuCao() != null ? String.valueOf(user.getChieuCao()) : "");
        etProfileWeight.setText(user.getCanNang() != null ? String.valueOf(user.getCanNang()) : "");
    }

    private void setupListeners() {
        btnBackProfileDetail.setOnClickListener(v -> finish());

        btnSaveProfile.setOnClickListener(v -> {
            String name = etProfileFullName.getText().toString().trim();
            String gender = etProfileGender.getText().toString().trim();
            String ageOrDob = etProfileAge.getText().toString().trim();
            String heightStr = etProfileHeight.getText().toString().trim();
            String weightStr = etProfileWeight.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                etProfileFullName.setError("Vui lòng nhập họ và tên");
                return;
            }

            Double height = null;
            if (!TextUtils.isEmpty(heightStr)) {
                try {
                    height = Double.parseDouble(heightStr);
                } catch (NumberFormatException e) {
                    etProfileHeight.setError("Chiều cao không hợp lệ");
                    return;
                }
            }

            Double weight = null;
            if (!TextUtils.isEmpty(weightStr)) {
                try {
                    weight = Double.parseDouble(weightStr);
                } catch (NumberFormatException e) {
                    etProfileWeight.setError("Cân nặng không hợp lệ");
                    return;
                }
            }

            String dob = currentBirthDate;
            if (!TextUtils.isEmpty(ageOrDob) && ageOrDob.contains("-")) {
                dob = ageOrDob;
            }

            if (currentUserId == null) {
                Toast.makeText(this, "Không tìm thấy ID người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            btnSaveProfile.setEnabled(false);
            btnSaveProfile.setText("Đang lưu...");

            UpdateUserRequest request = new UpdateUserRequest(name, currentPhone, gender, dob, height, weight);

            ApiClient.getApiService().updateUser("Bearer " + currentToken, currentUserId, request)
                    .enqueue(new Callback<ApiResponse<UserResponse>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                            btnSaveProfile.setEnabled(true);
                            btnSaveProfile.setText("Lưu thông tin");

                            if (response.code() == 401 || response.code() == 403) {
                                redirectToLogin("Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.");
                                return;
                            }

                            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                UserResponse updatedUser = response.body().getData();
                                if (updatedUser != null) {
                                    bindUserData(updatedUser);
                                    TokenManager.getInstance(ProfileDetailActivity.this).saveSession(
                                            currentToken, updatedUser.getId(), updatedUser.getEmail(), updatedUser.getHoTen()
                                    );
                                }
                                String successMsg = response.body().getMessage();
                                if (TextUtils.isEmpty(successMsg)) {
                                    successMsg = "Cập nhật thông tin thành công!";
                                }
                                Toast.makeText(ProfileDetailActivity.this, successMsg, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                String errorMsg = "Cập nhật thông tin thất bại!";
                                if (response.body() != null && !TextUtils.isEmpty(response.body().getMessage())) {
                                    errorMsg = response.body().getMessage();
                                }
                                Toast.makeText(ProfileDetailActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                            btnSaveProfile.setEnabled(true);
                            btnSaveProfile.setText("Lưu thông tin");
                            Toast.makeText(ProfileDetailActivity.this, "Không thể kết nối đến máy chủ: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    private void redirectToLogin(String message) {
        SharedPreferences prefs = getSharedPreferences("NutriBudgetPrefs", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
        TokenManager.getInstance(this).clearSession();

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
