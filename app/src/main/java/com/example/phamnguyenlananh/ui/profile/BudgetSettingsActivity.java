package com.example.phamnguyenlananh.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.data.MockDataRepository;
import com.example.phamnguyenlananh.data.api.ApiClient;
import com.example.phamnguyenlananh.data.api.NutriBudgetApiService;
import com.example.phamnguyenlananh.data.api.TokenManager;
import com.example.phamnguyenlananh.data.api.model.ApiResponse;
import com.example.phamnguyenlananh.data.api.model.BudgetRequest;
import com.example.phamnguyenlananh.data.api.model.BudgetResponse;
import com.example.phamnguyenlananh.ui.auth.LoginActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetSettingsActivity extends AppCompatActivity {
    private static final String TAG = "BudgetSettingsActivity";

    private ImageView btnBackBudget;
    private TextView tvCurrentBudgetBig, tvProjectedDay, tvProjectedWeek, tvProjectedMonth;
    private EditText etDailyBudget;
    private Button btnBudgetDecrease, btnBudgetIncrease, btnSaveBudget;

    private NutriBudgetApiService apiService;
    private TokenManager tokenManager;

    private Long existingBudgetId = null;
    private boolean suppressTextWatcher = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_settings);

        tokenManager = TokenManager.getInstance(this);
        apiService = ApiClient.getNutriBudgetApiService(this);

        if (!tokenManager.isLoggedIn()) {
            redirectToLogin("Phiên đăng nhập không hợp lệ. Vui lòng đăng nhập lại.");
            return;
        }

        initViews();
        setupListeners();
        loadCurrentData();
    }

    private void initViews() {
        btnBackBudget = findViewById(R.id.btnBackBudget);
        tvCurrentBudgetBig = findViewById(R.id.tvCurrentBudgetBig);
        tvProjectedDay = findViewById(R.id.tvProjectedDay);
        tvProjectedWeek = findViewById(R.id.tvProjectedWeek);
        tvProjectedMonth = findViewById(R.id.tvProjectedMonth);
        etDailyBudget = findViewById(R.id.etDailyBudget);
        btnBudgetDecrease = findViewById(R.id.btnBudgetDecrease);
        btnBudgetIncrease = findViewById(R.id.btnBudgetIncrease);
        btnSaveBudget = findViewById(R.id.btnSaveBudget);
    }

    private void loadCurrentData() {
        long userId = tokenManager.getUserId();
        Log.d(TAG, "Loading budget for userId: " + userId);

        btnSaveBudget.setEnabled(false);
        btnSaveBudget.setText("Đang tải...");

        apiService.getBudgets(userId).enqueue(new Callback<ApiResponse<List<BudgetResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BudgetResponse>>> call,
                                   Response<ApiResponse<List<BudgetResponse>>> response) {
                btnSaveBudget.setEnabled(true);
                btnSaveBudget.setText("Lưu ngân sách");

                if (response.code() == 401 || response.code() == 403) {
                    redirectToLogin("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.");
                    return;
                }

                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess()
                        && response.body().getData() != null
                        && !response.body().getData().isEmpty()) {

                    BudgetResponse b = response.body().getData().get(0);
                    existingBudgetId = b.getId();

                    long daily = b.getNganSachNgay() != null ? b.getNganSachNgay().longValue() : 100000;
                    Long weekly = b.getNganSachTuan() != null ? b.getNganSachTuan().longValue() : null;
                    Long monthly = b.getNganSachThang() != null ? b.getNganSachThang().longValue() : null;

                    Log.d(TAG, "Loaded budget: id=" + existingBudgetId + " daily=" + daily + " weekly=" + weekly + " monthly=" + monthly);

                    suppressTextWatcher = true;
                    etDailyBudget.setText(String.valueOf(daily));
                    suppressTextWatcher = false;

                    updateCalculations(daily, weekly, monthly);
                } else {
                    existingBudgetId = null;
                    Log.d(TAG, "No existing budget found for user, ready to create new.");
                    Toast.makeText(BudgetSettingsActivity.this, "Chưa thiết lập ngân sách. Hãy nhập mức ngân sách và nhấn Lưu.", Toast.LENGTH_SHORT).show();
                    updateCalculations(100000, null, null);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<BudgetResponse>>> call, Throwable t) {
                btnSaveBudget.setEnabled(true);
                btnSaveBudget.setText("Lưu ngân sách");
                Log.e(TAG, "Load budget failed: " + t.getMessage());
                Toast.makeText(BudgetSettingsActivity.this, "Không thể tải ngân sách: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCalculations(long daily, Long weekly, Long monthly) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvCurrentBudgetBig.setText(nf.format(daily) + " đ/ngày");
        tvProjectedDay.setText(nf.format(daily) + " đ");

        long w = weekly != null ? weekly : daily * 7;
        long m = monthly != null ? monthly : daily * 30;

        tvProjectedWeek.setText(nf.format(w) + " đ");
        tvProjectedMonth.setText(nf.format(m) + " đ");
    }

    private void setupListeners() {
        btnBackBudget.setOnClickListener(v -> finish());

        btnBudgetDecrease.setOnClickListener(v -> {
            long current = getCurrentInputAmount();
            long newAmount = Math.max(5000, current - 5000);
            etDailyBudget.setText(String.valueOf(newAmount));
            updateCalculations(newAmount, null, null);
        });

        btnBudgetIncrease.setOnClickListener(v -> {
            long current = getCurrentInputAmount();
            long newAmount = current + 5000;
            etDailyBudget.setText(String.valueOf(newAmount));
            updateCalculations(newAmount, null, null);
        });

        etDailyBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (suppressTextWatcher) return;

                if (!TextUtils.isEmpty(s)) {
                    try {
                        long amount = Long.parseLong(s.toString().trim());
                        if (amount > 0) {
                            updateCalculations(amount, null, null);
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Quick presets
        findViewById(R.id.chipBudget50).setOnClickListener(v -> setPresetAmount(50000));
        findViewById(R.id.chipBudget70).setOnClickListener(v -> setPresetAmount(70000));
        findViewById(R.id.chipBudget100).setOnClickListener(v -> setPresetAmount(100000));
        findViewById(R.id.chipBudget120).setOnClickListener(v -> setPresetAmount(120000));
        findViewById(R.id.chipBudget150).setOnClickListener(v -> setPresetAmount(150000));

        btnSaveBudget.setOnClickListener(v -> saveBudget());
    }

    private void setPresetAmount(long amount) {
        etDailyBudget.setText(String.valueOf(amount));
        updateCalculations(amount, null, null);
    }

    private long getCurrentInputAmount() {
        String s = etDailyBudget.getText().toString().trim();
        if (TextUtils.isEmpty(s)) return 100000;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return 100000;
        }
    }

    private void saveBudget() {
        String s = etDailyBudget.getText().toString().trim();

        // 1. Validate: Không bỏ trống
        if (TextUtils.isEmpty(s)) {
            etDailyBudget.setError("Vui lòng nhập số tiền ngân sách");
            etDailyBudget.requestFocus();
            return;
        }

        // 2. Validate: Dữ liệu số hợp lệ
        long dailyAmount;
        try {
            dailyAmount = Long.parseLong(s);
        } catch (NumberFormatException e) {
            etDailyBudget.setError("Số tiền không hợp lệ");
            etDailyBudget.requestFocus();
            return;
        }

        // 3. Validate: Số tiền phải lớn hơn 0
        if (dailyAmount <= 0) {
            etDailyBudget.setError("Số tiền ngân sách phải lớn hơn 0");
            etDailyBudget.requestFocus();
            return;
        }

        long userId = tokenManager.getUserId();
        double daily = (double) dailyAmount;
        double weekly = daily * 7.0;
        double monthly = daily * 30.0;
        BudgetRequest req = new BudgetRequest(userId, daily, weekly, monthly);

        btnSaveBudget.setEnabled(false);
        btnSaveBudget.setText("Đang lưu...");

        Callback<ApiResponse<BudgetResponse>> saveCallback = new Callback<ApiResponse<BudgetResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<BudgetResponse>> call, Response<ApiResponse<BudgetResponse>> response) {
                btnSaveBudget.setEnabled(true);
                btnSaveBudget.setText("Lưu ngân sách");

                if (response.code() == 401 || response.code() == 403) {
                    redirectToLogin("Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.");
                    return;
                }

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    BudgetResponse saved = response.body().getData();
                    if (saved != null) {
                        existingBudgetId = saved.getId();
                    }

                    // Update local mock/cache repository for Home & Profile views
                    try {
                        MockDataRepository.getInstance().getBudgetPlan().setDailyBudget((int) dailyAmount);
                    } catch (Exception ignored) {}

                    String msg = response.body().getMessage() != null
                            ? response.body().getMessage()
                            : "Cập nhật ngân sách thành công!";
                    Toast.makeText(BudgetSettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Saved budget: id=" + (saved != null ? saved.getId() : "null"));

                    setResult(RESULT_OK);
                    finish();
                } else {
                    String errorMsg = "Lưu ngân sách thất bại";
                    if (response.body() != null && response.body().getMessage() != null) {
                        errorMsg = response.body().getMessage();
                    }
                    Toast.makeText(BudgetSettingsActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Save budget failed: " + errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<BudgetResponse>> call, Throwable t) {
                btnSaveBudget.setEnabled(true);
                btnSaveBudget.setText("Lưu ngân sách");
                Toast.makeText(BudgetSettingsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Save budget error: " + t.getMessage());
            }
        };

        if (existingBudgetId != null) {
            Log.d(TAG, "Updating existing budget id=" + existingBudgetId);
            apiService.updateBudget(existingBudgetId, req).enqueue(saveCallback);
        } else {
            Log.d(TAG, "Creating new budget for userId=" + userId);
            apiService.createBudget(req).enqueue(saveCallback);
        }
    }

    private void redirectToLogin(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        tokenManager.clearSession();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}