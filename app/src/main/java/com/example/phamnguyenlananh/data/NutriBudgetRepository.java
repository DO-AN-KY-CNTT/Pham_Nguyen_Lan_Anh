package com.example.phamnguyenlananh.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.data.api.ApiCallback;
import com.example.phamnguyenlananh.data.api.ApiClient;
import com.example.phamnguyenlananh.data.api.NutriBudgetApiService;
import com.example.phamnguyenlananh.data.api.TokenManager;
import com.example.phamnguyenlananh.data.api.model.*;
import com.example.phamnguyenlananh.model.*;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NutriBudgetRepository {

    private static NutriBudgetRepository instance;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private NutriBudgetRepository() {}

    public static synchronized NutriBudgetRepository getInstance() {
        if (instance == null) {
            instance = new NutriBudgetRepository();
        }
        return instance;
    }

    private NutriBudgetApiService getService(Context context) {
        return ApiClient.getNutriBudgetApiService(context);
    }

    // 1. Auth: Login
    public void login(Context context, String email, String password, ApiCallback<AuthResponse> callback) {
        getService(context).login(new LoginRequest(email, password)).enqueue(new Callback<ApiResponse<AuthResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthResponse>> call, Response<ApiResponse<AuthResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    AuthResponse auth = response.body().getData();
                    TokenManager.getInstance(context).saveSession(
                            auth.getToken(),
                            auth.getUser() != null ? auth.getUser().getId() : 1L,
                            auth.getUser() != null ? auth.getUser().getEmail() : email,
                            auth.getUser() != null ? auth.getUser().getHoTen() : "Nguoi dung"
                    );

                    // Sync user profile into local repository
                    if (auth.getUser() != null) {
                        applyUserResponse(auth.getUser());
                    }

                    // Trigger sync of other data
                    syncAllData(context, null);

                    callback.onSuccess(auth);
                } else {
                    String msg = "Dang nhap that bai! Vui long kiem tra email hoac mat khau.";
                    if (response.body() != null && response.body().getMessage() != null) {
                        msg = response.body().getMessage();
                    }
                    callback.onError(msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                callback.onError("Khong the ket noi den may chu: " + t.getMessage());
            }
        });
    }

    // 2. Auth: Register
    public void register(Context context, RegisterRequest request, ApiCallback<AuthResponse> callback) {
        getService(context).register(request).enqueue(new Callback<ApiResponse<AuthResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthResponse>> call, Response<ApiResponse<AuthResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    AuthResponse auth = response.body().getData();
                    TokenManager.getInstance(context).saveSession(
                            auth.getToken(),
                            auth.getUser() != null ? auth.getUser().getId() : 1L,
                            auth.getUser() != null ? auth.getUser().getEmail() : request.getEmail(),
                            auth.getUser() != null ? auth.getUser().getHoTen() : request.getHoTen()
                    );

                    if (auth.getUser() != null) {
                        applyUserResponse(auth.getUser());
                    }

                    callback.onSuccess(auth);
                } else {
                    String msg = "Dang ky that bai!";
                    if (response.body() != null && response.body().getMessage() != null) {
                        msg = response.body().getMessage();
                    }
                    callback.onError(msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                callback.onError("Loi ket noi: " + t.getMessage());
            }
        });
    }

    // 3. Update User Profile
    public void updateUserProfile(Context context, UpdateUserRequest request, ApiCallback<UserResponse> callback) {
        long userId = TokenManager.getInstance(context).getUserId();
        getService(context).updateUser(userId, request).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    UserResponse u = response.body().getData();
                    applyUserResponse(u);
                    callback.onSuccess(u);
                } else {
                    callback.onError(response.body() != null ? response.body().getMessage() : "Cap nhat that bai");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                callback.onError("Loi ket noi: " + t.getMessage());
            }
        });
    }

    // 4. Save Nutrition Goal
    public void saveNutritionGoal(Context context, NutritionGoalRequest request, ApiCallback<NutritionGoalResponse> callback) {
        getService(context).createNutritionGoal(request).enqueue(new Callback<ApiResponse<NutritionGoalResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<NutritionGoalResponse>> call, Response<ApiResponse<NutritionGoalResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    NutritionGoalResponse g = response.body().getData();
                    applyGoalResponse(g);
                    callback.onSuccess(g);
                } else {
                    callback.onError(response.body() != null ? response.body().getMessage() : "Thiet lap muc tieu that bai");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<NutritionGoalResponse>> call, Throwable t) {
                callback.onError("Loi ket noi: " + t.getMessage());
            }
        });
    }

    // 5. Save Budget
    public void saveBudget(Context context, BudgetRequest request, ApiCallback<BudgetResponse> callback) {
        getService(context).createBudget(request).enqueue(new Callback<ApiResponse<BudgetResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<BudgetResponse>> call, Response<ApiResponse<BudgetResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    BudgetResponse b = response.body().getData();
                    applyBudgetResponse(b);
                    callback.onSuccess(b);
                } else {
                    callback.onError(response.body() != null ? response.body().getMessage() : "Thiet lap ngan sach that bai");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<BudgetResponse>> call, Throwable t) {
                callback.onError("Loi ket noi: " + t.getMessage());
            }
        });
    }

    // 6. Sync All Data from API to MockDataRepository
    public void syncAllData(Context context, Runnable onComplete) {
        long userId = TokenManager.getInstance(context).getUserId();
        NutriBudgetApiService service = getService(context);

        // Fetch User Profile
        service.getUserById(userId).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    applyUserResponse(response.body().getData());
                }
            }
            @Override public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {}
        });

        // Fetch Nutrition Goals
        service.getNutritionGoals(userId).enqueue(new Callback<ApiResponse<List<NutritionGoalResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<NutritionGoalResponse>>> call, Response<ApiResponse<List<NutritionGoalResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    applyGoalResponse(response.body().getData().get(0));
                }
            }
            @Override public void onFailure(Call<ApiResponse<List<NutritionGoalResponse>>> call, Throwable t) {}
        });

        // Fetch Budgets
        service.getBudgets(userId).enqueue(new Callback<ApiResponse<List<BudgetResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BudgetResponse>>> call, Response<ApiResponse<List<BudgetResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    applyBudgetResponse(response.body().getData().get(0));
                }
            }
            @Override public void onFailure(Call<ApiResponse<List<BudgetResponse>>> call, Throwable t) {}
        });

        // Fetch Dishes
        service.getDishes(null).enqueue(new Callback<ApiResponse<List<DishResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<DishResponse>>> call, Response<ApiResponse<List<DishResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    applyDishesResponse(response.body().getData());
                }
            }
            @Override public void onFailure(Call<ApiResponse<List<DishResponse>>> call, Throwable t) {}
        });

        // Fetch Menus
        service.getMenus(userId).enqueue(new Callback<ApiResponse<List<MenuResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<MenuResponse>>> call, Response<ApiResponse<List<MenuResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    applyMenusResponse(response.body().getData());
                }
                if (onComplete != null) {
                    mainHandler.post(onComplete);
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<MenuResponse>>> call, Throwable t) {
                if (onComplete != null) {
                    mainHandler.post(onComplete);
                }
            }
        });
    }

    // Helper mapper methods
    private void applyUserResponse(UserResponse u) {
        UserProfile p = MockDataRepository.getInstance().getUserProfile();
        if (u.getHoTen() != null) p.setFullName(u.getHoTen());
        if (u.getEmail() != null) p.setEmail(u.getEmail());
        if (u.getSoDienThoai() != null) p.setPhone(u.getSoDienThoai());
        if (u.getChieuCao() != null) p.setHeightCm(u.getChieuCao().intValue());
        if (u.getCanNang() != null) p.setWeightKg(u.getCanNang());
        if (u.getGioiTinh() != null) p.setGender(u.getGioiTinh());
    }

    private void applyGoalResponse(NutritionGoalResponse g) {
        NutritionGoal goal = MockDataRepository.getInstance().getNutritionGoal();
        if (g.getCaloMucTieu() != null) goal.setTargetCalories(g.getCaloMucTieu().intValue());
        if (g.getProteinMucTieu() != null) goal.setProteinGrams(g.getProteinMucTieu().intValue());
        if (g.getCarbMucTieu() != null) goal.setCarbsGrams(g.getCarbMucTieu().intValue());
        if (g.getFatMucTieu() != null) goal.setFatGrams(g.getFatMucTieu().intValue());
        if (g.getMucTieu() != null) goal.setGoalType(g.getMucTieu());
    }

    private void applyBudgetResponse(BudgetResponse b) {
        BudgetPlan plan = MockDataRepository.getInstance().getBudgetPlan();
        if (b.getNganSachNgay() != null) plan.setDailyBudget(b.getNganSachNgay().intValue());
        if (b.getNganSachThang() != null) plan.setMonthlyBudget(b.getNganSachThang().intValue());
    }

    private void applyDishesResponse(List<DishResponse> dishes) {
        List<AlternativeDish> alts = MockDataRepository.getInstance().getAlternativeDishes();
        alts.clear();
        for (int i = 0; i < dishes.size(); i++) {
            DishResponse d = dishes.get(i);
            int imageRes = R.drawable.img_dish_pork;
            if (i % 4 == 1) imageRes = R.drawable.img_dish_fish;
            else if (i % 4 == 2) imageRes = R.drawable.img_dish_bento;
            else if (i % 4 == 3) imageRes = R.drawable.img_dish_salmon;

            alts.add(new AlternativeDish(
                    String.valueOf(d.getId()),
                    d.getTenMon(),
                    d.getCalo() != null ? d.getCalo().intValue() : 450,
                    d.getProtein() != null ? d.getProtein().intValue() : 25,
                    d.getGiaDuKien() != null ? d.getGiaDuKien().intValue() : 35000,
                    imageRes,
                    d.getKhauPhan() != null ? d.getKhauPhan() : "1 phan",
                    "Mon truyen thong"
            ));
        }
    }

    private void applyMenusResponse(List<MenuResponse> menus) {
        if (menus.isEmpty()) return;
        MenuResponse topMenu = menus.get(0);

        List<MealItem> meals = new ArrayList<>();
        if (topMenu.getChiTietThucDon() != null) {
            for (MenuDetailResponse item : topMenu.getChiTietThucDon()) {
                int img = R.drawable.img_dish_pork;
                String bua = item.getBuaAn();
                if ("SANG".equalsIgnoreCase(bua)) img = R.drawable.img_dish_oats;
                else if ("TRUA".equalsIgnoreCase(bua)) img = R.drawable.img_dish_salmon;
                else if ("TOI".equalsIgnoreCase(bua)) img = R.drawable.img_dish_chicken;
                else if ("PHU".equalsIgnoreCase(bua)) img = R.drawable.img_dish_bento;

                meals.add(new MealItem(
                        String.valueOf(item.getId()),
                        bua != null ? "Bua " + bua.toLowerCase() : "Bua an",
                        item.getTenMon(),
                        item.getKhauPhan() != null ? item.getKhauPhan() : "1 phan",
                        item.getCalo() != null ? item.getCalo().intValue() : 400,
                        25,
                        50,
                        12,
                        item.getChiPhi() != null ? item.getChiPhi().intValue() : 30000,
                        img
                ));
            }
        }

        if (!meals.isEmpty()) {
            MealPlan current = MockDataRepository.getInstance().getCurrentMealPlan();
            current.setTitle(topMenu.getTenThucDon());
            current.setMeals(meals);
            current.recalculateTotals();

            // Also add all fetched menus to history
            List<MealPlan> history = MockDataRepository.getInstance().getMealHistory();
            history.clear();
            for (MenuResponse m : menus) {
                history.add(new MealPlan(
                        String.valueOf(m.getId()),
                        m.getTenThucDon(),
                        "Ngay ap dung: " + m.getNgayApDung(),
                        95,
                        m.getTongChiPhi() != null ? m.getTongChiPhi().intValue() : 80000,
                        m.getTongCalo() != null ? m.getTongCalo().intValue() : 1800,
                        m.getTongProtein() != null ? m.getTongProtein().intValue() : 80,
                        m.getTongCarb() != null ? m.getTongCarb().intValue() : 190,
                        m.getTongFat() != null ? m.getTongFat().intValue() : 50,
                        m.getTongChiPhi() != null ? m.getTongChiPhi().intValue() : 80000,
                        3,
                        m.getTrangThai(),
                        m.getNgayApDung(),
                        R.drawable.img_dish_pork,
                        meals
                ));
            }
        }
    }
}
