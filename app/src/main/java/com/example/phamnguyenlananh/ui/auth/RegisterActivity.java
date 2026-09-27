package com.example.phamnguyenlananh.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.data.api.ApiClient;
import com.example.phamnguyenlananh.data.api.model.RegisterRequest;
import com.example.phamnguyenlananh.data.api.model.RegisterResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etFullName, etRegisterEmail, etPhone, etRegisterPassword, etConfirmPassword;
    private Button btnRegisterSubmit;
    private TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.etFullName);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etPhone = findViewById(R.id.etPhone);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        // Nút Đăng ký tài khoản
        btnRegisterSubmit.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String email = etRegisterEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String pass = etRegisterPassword.getText().toString().trim();
            String confirm = etConfirmPassword.getText().toString().trim();

            // Kiểm tra các trường bắt buộc
            if (TextUtils.isEmpty(name)) {
                etFullName.setError("Vui lòng nhập họ và tên");
                etFullName.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(email)) {
                etRegisterEmail.setError("Vui lòng nhập email");
                etRegisterEmail.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etRegisterEmail.setError("Email không đúng định dạng");
                etRegisterEmail.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                etRegisterPassword.setError("Vui lòng nhập mật khẩu");
                etRegisterPassword.requestFocus();
                return;
            }
            if (pass.length() < 6) {
                etRegisterPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
                etRegisterPassword.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(confirm)) {
                etConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
                etConfirmPassword.requestFocus();
                return;
            }
            if (!pass.equals(confirm)) {
                etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
                etConfirmPassword.requestFocus();
                return;
            }

            btnRegisterSubmit.setEnabled(false);
            btnRegisterSubmit.setText("Đang đăng ký...");

            // Gọi API Đăng ký POST /api/auth/register qua Retrofit ApiClient
            RegisterRequest req = new RegisterRequest(name, email, pass, phone.isEmpty() ? null : phone);
            ApiClient.getApiService().register(req).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    btnRegisterSubmit.setEnabled(true);
                    btnRegisterSubmit.setText("Đăng ký tài khoản");

                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        String successMsg = response.body().getMessage();
                        if (TextUtils.isEmpty(successMsg)) {
                            successMsg = "Đăng ký tài khoản thành công!";
                        }
                        Toast.makeText(RegisterActivity.this, successMsg, Toast.LENGTH_SHORT).show();

                        // Chuyển về màn hình Đăng nhập (không tự động đăng nhập)
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        // Hiển thị lỗi từ backend API (ví dụ: "Email da duoc dang ky: ...")
                        String errMsg = "Đăng ký thất bại! Vui lòng thử lại.";
                        if (response.body() != null && !TextUtils.isEmpty(response.body().getMessage())) {
                            errMsg = response.body().getMessage();
                        } else if (response.errorBody() != null) {
                            try {
                                String errJson = response.errorBody().string();
                                RegisterResponse errObj = new Gson().fromJson(errJson, RegisterResponse.class);
                                if (errObj != null && !TextUtils.isEmpty(errObj.getMessage())) {
                                    errMsg = errObj.getMessage();
                                }
                            } catch (Exception ignored) {}
                        }
                        Toast.makeText(RegisterActivity.this, errMsg, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    btnRegisterSubmit.setEnabled(true);
                    btnRegisterSubmit.setText("Đăng ký tài khoản");
                    Toast.makeText(RegisterActivity.this, "Không thể kết nối đến máy chủ: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        // Quay lại màn hình đăng nhập
        tvBackToLogin.setOnClickListener(v -> finish());
    }
}
