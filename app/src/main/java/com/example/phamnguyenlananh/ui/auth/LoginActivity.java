package com.example.phamnguyenlananh.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phamnguyenlananh.MainActivity;
import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.data.api.ApiClient;
import com.example.phamnguyenlananh.data.api.TokenManager;
import com.example.phamnguyenlananh.data.api.model.LoginRequest;
import com.example.phamnguyenlananh.data.api.model.LoginResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoogleLogin;
    private TextView tvRegister, tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Pre-fill demo user
        etEmail.setText("lananh@nutribudget.com");
        etPassword.setText("123456");

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Vui lòng nhập email");
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                etPassword.setError("Vui lòng nhập mật khẩu");
                return;
            }

            btnLogin.setEnabled(false);
            btnLogin.setText("Đang đăng nhập...");

            // Gọi API đăng nhập trực tiếp qua ApiService
            LoginRequest request = new LoginRequest(email, pass);
            ApiClient.getApiService().login(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Đăng nhập");

                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        LoginResponse loginResponse = response.body();
                        String token = loginResponse.getToken();

                        // 1. Lưu JWT token bằng SharedPreferences theo yêu cầu
                        SharedPreferences prefs = getSharedPreferences("NutriBudgetPrefs", Context.MODE_PRIVATE);
                        prefs.edit().putString("jwt_token", token).apply();

                        // 2. Lưu đồng thời qua TokenManager để đồng bộ phiên làm việc toàn ứng dụng
                        Long userId = (loginResponse.getUser() != null && loginResponse.getUser().getId() != null)
                                ? loginResponse.getUser().getId() : 1L;
                        String userEmail = (loginResponse.getUser() != null && loginResponse.getUser().getEmail() != null)
                                ? loginResponse.getUser().getEmail() : email;
                        String userName = (loginResponse.getUser() != null && loginResponse.getUser().getHoTen() != null)
                                ? loginResponse.getUser().getHoTen() : "Bạn";

                        TokenManager.getInstance(LoginActivity.this).saveSession(
                                token, userId, userEmail, userName
                        );

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công! Chào " + userName + "!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Khi đăng nhập thất bại, hiển thị thông báo lỗi
                        String errMsg = "Đăng nhập thất bại! Vui lòng kiểm tra lại email hoặc mật khẩu.";
                        if (response.body() != null && response.body().getMessage() != null) {
                            errMsg = response.body().getMessage();
                        } else if (response.errorBody() != null) {
                            try {
                                String errJson = response.errorBody().string();
                                LoginResponse errObj = new Gson().fromJson(errJson, LoginResponse.class);
                                if (errObj != null && errObj.getMessage() != null && !errObj.getMessage().isEmpty()) {
                                    errMsg = errObj.getMessage();
                                }
                            } catch (Exception ignored) {}
                        }
                        Toast.makeText(LoginActivity.this, errMsg, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Đăng nhập");
                    Toast.makeText(LoginActivity.this, "Không thể kết nối đến máy chủ: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        btnGoogleLogin.setOnClickListener(v -> {
            Toast.makeText(this, "Đang kết nối tài khoản Google...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Link khôi phục mật khẩu đã được gửi đến email của bạn.", Toast.LENGTH_LONG).show();
        });
    }
}
