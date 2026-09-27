package com.example.phamnguyenlananh.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.data.api.ApiClient;
import com.example.phamnguyenlananh.data.api.NutriBudgetApiService;
import com.example.phamnguyenlananh.data.api.TokenManager;
import com.example.phamnguyenlananh.data.api.model.ApiResponse;
import com.example.phamnguyenlananh.data.api.model.NutritionGoalRequest;
import com.example.phamnguyenlananh.data.api.model.NutritionGoalResponse;
import com.example.phamnguyenlananh.ui.auth.LoginActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NutritionGoalsActivity extends AppCompatActivity {
    private static final String TAG = "NutritionGoalsActivity";

    private ImageView btnBackGoals;
    private RadioGroup rgGoalType;
    private RadioButton rbWeightLoss, rbMuscleGain, rbMaintenance, rbWeightGain;
    private EditText etTargetCalories, etProtein, etCarbs, etFat;
    private Button btnSaveNutritionGoal;

    private NutriBudgetApiService apiService;
    private TokenManager tokenManager;

    // Track existing goal for PUT vs POST
    private Long existingGoalId = null;
    private boolean suppressRadioListener = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_goals);

        tokenManager = TokenManager.getInstance(this);
        apiService = ApiClient.getNutriBudgetApiService(this);

        // Check auth
        if (!tokenManager.isLoggedIn()) {
            redirectToLogin("Phien dang nhap khong hop le. Vui long dang nhap lai.");
            return;
        }

        initViews();
        setupListeners();
        loadCurrentData();
    }

    private void initViews() {
        btnBackGoals = findViewById(R.id.btnBackGoals);
        rgGoalType = findViewById(R.id.rgGoalType);
        rbWeightLoss = findViewById(R.id.rbWeightLoss);
        rbMuscleGain = findViewById(R.id.rbMuscleGain);
        rbMaintenance = findViewById(R.id.rbMaintenance);
        rbWeightGain = findViewById(R.id.rbWeightGain);
        etTargetCalories = findViewById(R.id.etTargetCalories);
        etProtein = findViewById(R.id.etProtein);
        etCarbs = findViewById(R.id.etCarbs);
        etFat = findViewById(R.id.etFat);
        btnSaveNutritionGoal = findViewById(R.id.btnSaveNutritionGoal);
    }

    private void loadCurrentData() {
        long userId = tokenManager.getUserId();
        Log.d(TAG, "Loading nutrition goals for userId: " + userId);

        btnSaveNutritionGoal.setEnabled(false);
        btnSaveNutritionGoal.setText("Dang tai...");

        apiService.getNutritionGoals(userId).enqueue(new Callback<ApiResponse<List<NutritionGoalResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<NutritionGoalResponse>>> call,
                                   Response<ApiResponse<List<NutritionGoalResponse>>> response) {
                btnSaveNutritionGoal.setEnabled(true);
                btnSaveNutritionGoal.setText("L\u01B0u m\u1EE5c ti\u00EAu");

                if (response.code() == 401 || response.code() == 403) {
                    redirectToLogin("Phien dang nhap da het han. Vui long dang nhap lai.");
                    return;
                }

                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess()
                        && response.body().getData() != null
                        && !response.body().getData().isEmpty()) {

                    NutritionGoalResponse goal = response.body().getData().get(0);
                    existingGoalId = goal.getId();

                    Log.d(TAG, "Loaded existing goal id=" + existingGoalId
                            + " mucTieu=" + goal.getMucTieu()
                            + " calo=" + goal.getCaloMucTieu()
                            + " protein=" + goal.getProteinMucTieu()
                            + " carb=" + goal.getCarbMucTieu()
                            + " fat=" + goal.getFatMucTieu());

                    populateForm(goal);
                } else {
                    existingGoalId = null;
                    Log.d(TAG, "No existing nutrition goal found, will create new.");
                    Toast.makeText(NutritionGoalsActivity.this, "Chưa có mục tiêu dinh dưỡng, hãy thiết lập mới.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<NutritionGoalResponse>>> call, Throwable t) {
                btnSaveNutritionGoal.setEnabled(true);
                btnSaveNutritionGoal.setText("L\u01B0u m\u1EE5c ti\u00EAu");
                Log.e(TAG, "Failed to load nutrition goals: " + t.getMessage());
                Toast.makeText(NutritionGoalsActivity.this,
                        "Khong the tai muc tieu dinh duong: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateForm(NutritionGoalResponse goal) {
        suppressRadioListener = true;

        if (goal.getCaloMucTieu() != null) {
            etTargetCalories.setText(String.valueOf(goal.getCaloMucTieu().intValue()));
        }
        if (goal.getProteinMucTieu() != null) {
            etProtein.setText(String.valueOf(goal.getProteinMucTieu().intValue()));
        }
        if (goal.getCarbMucTieu() != null) {
            etCarbs.setText(String.valueOf(goal.getCarbMucTieu().intValue()));
        }
        if (goal.getFatMucTieu() != null) {
            etFat.setText(String.valueOf(goal.getFatMucTieu().intValue()));
        }

        String mucTieu = goal.getMucTieu();
        if (mucTieu != null) {
            String lower = mucTieu.toLowerCase();
            if (lower.contains("giam") || lower.contains("gi\u1EA3m")) {
                rbWeightLoss.setChecked(true);
            } else if (lower.contains("tang co") || lower.contains("t\u0103ng c\u01A1")) {
                rbMuscleGain.setChecked(true);
            } else if (lower.contains("duy tri") || lower.contains("duy tr\u00EC")) {
                rbMaintenance.setChecked(true);
            } else if (lower.contains("tang can") || lower.contains("t\u0103ng c\u00E2n")) {
                rbWeightGain.setChecked(true);
            } else {
                rbMuscleGain.setChecked(true);
            }
        }

        suppressRadioListener = false;
    }

    private void setupListeners() {
        btnBackGoals.setOnClickListener(v -> finish());

        rgGoalType.setOnCheckedChangeListener((group, checkedId) -> {
            if (suppressRadioListener) return;

            if (checkedId == R.id.rbWeightLoss) {
                etTargetCalories.setText("1600");
                etProtein.setText("115");
                etCarbs.setText("160");
                etFat.setText("45");
            } else if (checkedId == R.id.rbMuscleGain) {
                etTargetCalories.setText("2000");
                etProtein.setText("130");
                etCarbs.setText("220");
                etFat.setText("55");
            } else if (checkedId == R.id.rbMaintenance) {
                etTargetCalories.setText("1850");
                etProtein.setText("105");
                etCarbs.setText("210");
                etFat.setText("50");
            } else if (checkedId == R.id.rbWeightGain) {
                etTargetCalories.setText("2300");
                etProtein.setText("125");
                etCarbs.setText("270");
                etFat.setText("65");
            }
        });

        btnSaveNutritionGoal.setOnClickListener(v -> saveNutritionGoal());
    }

    private void saveNutritionGoal() {
        String calStr = etTargetCalories.getText().toString().trim();
        String pStr = etProtein.getText().toString().trim();
        String cStr = etCarbs.getText().toString().trim();
        String fStr = etFat.getText().toString().trim();

        if (TextUtils.isEmpty(calStr)) {
            etTargetCalories.setError("Vui long nhap calo muc tieu");
            etTargetCalories.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pStr)) {
            etProtein.setError("Vui long nhap luong protein");
            etProtein.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(cStr)) {
            etCarbs.setError("Vui long nhap luong carb");
            etCarbs.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(fStr)) {
            etFat.setError("Vui long nhap luong chat beo");
            etFat.requestFocus();
            return;
        }

        int cal, p, c, f;
        try {
            cal = Integer.parseInt(calStr);
            p = Integer.parseInt(pStr);
            c = Integer.parseInt(cStr);
            f = Integer.parseInt(fStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Gia tri dinh duong phai la so nguyen hop le", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cal <= 0) {
            etTargetCalories.setError("Calo phai lon hon 0");
            etTargetCalories.requestFocus();
            return;
        }
        if (p <= 0) {
            etProtein.setError("Protein phai lon hon 0");
            etProtein.requestFocus();
            return;
        }
        if (c <= 0) {
            etCarbs.setError("Carb phai lon hon 0");
            etCarbs.requestFocus();
            return;
        }
        if (f <= 0) {
            etFat.setError("Chat beo phai lon hon 0");
            etFat.requestFocus();
            return;
        }

        String goalType = "Tang co";
        int checkedId = rgGoalType.getCheckedRadioButtonId();
        if (checkedId == R.id.rbWeightLoss) goalType = "Giam can";
        else if (checkedId == R.id.rbMaintenance) goalType = "Duy tri can nang";
        else if (checkedId == R.id.rbWeightGain) goalType = "Tang can";

        long userId = tokenManager.getUserId();
        NutritionGoalRequest req = new NutritionGoalRequest(
                userId, goalType, (double) cal, (double) p, (double) c, (double) f);

        btnSaveNutritionGoal.setEnabled(false);
        btnSaveNutritionGoal.setText("Dang luu...");

        Callback<ApiResponse<NutritionGoalResponse>> saveCallback = new Callback<ApiResponse<NutritionGoalResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<NutritionGoalResponse>> call,
                                   Response<ApiResponse<NutritionGoalResponse>> response) {
                btnSaveNutritionGoal.setEnabled(true);
                btnSaveNutritionGoal.setText("L\u01B0u m\u1EE5c ti\u00EAu");

                if (response.code() == 401 || response.code() == 403) {
                    redirectToLogin("Phien lam viec da het han. Vui long dang nhap lai.");
                    return;
                }

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    NutritionGoalResponse saved = response.body().getData();
                    if (saved != null) {
                        existingGoalId = saved.getId();
                    }

                    String msg = response.body().getMessage() != null
                            ? response.body().getMessage()
                            : "Da luu muc tieu dinh duong thanh cong!";
                    Toast.makeText(NutritionGoalsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Saved nutrition goal: id=" + (saved != null ? saved.getId() : "null"));
                    setResult(RESULT_OK);
                    finish();
                } else {
                    String errorMsg = "Luu muc tieu that bai";
                    if (response.body() != null && response.body().getMessage() != null) {
                        errorMsg = response.body().getMessage();
                    }
                    Toast.makeText(NutritionGoalsActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Save nutrition goal failed: " + errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<NutritionGoalResponse>> call, Throwable t) {
                btnSaveNutritionGoal.setEnabled(true);
                btnSaveNutritionGoal.setText("L\u01B0u m\u1EE5c ti\u00EAu");
                Toast.makeText(NutritionGoalsActivity.this,
                        "Loi ket noi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Save nutrition goal error: " + t.getMessage());
            }
        };

        if (existingGoalId != null) {
            Log.d(TAG, "Updating existing goal id=" + existingGoalId);
            apiService.updateNutritionGoal(existingGoalId, req).enqueue(saveCallback);
        } else {
            Log.d(TAG, "Creating new nutrition goal");
            apiService.createNutritionGoal(req).enqueue(saveCallback);
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