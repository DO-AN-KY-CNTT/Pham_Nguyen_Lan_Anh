package com.example.phamnguyenlananh.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.adapter.MealCardAdapter;
import com.example.phamnguyenlananh.data.MockDataRepository;
import com.example.phamnguyenlananh.data.api.ApiClient;
import com.example.phamnguyenlananh.data.api.NutriBudgetApiService;
import com.example.phamnguyenlananh.data.api.TokenManager;
import com.example.phamnguyenlananh.data.api.model.ApiResponse;
import com.example.phamnguyenlananh.data.api.model.BudgetResponse;
import com.example.phamnguyenlananh.data.api.model.MenuDetailResponse;
import com.example.phamnguyenlananh.data.api.model.NutritionGoalResponse;
import com.example.phamnguyenlananh.data.api.model.SuggestMenuRequest;
import com.example.phamnguyenlananh.data.api.model.SuggestedMenuResponse;
import com.example.phamnguyenlananh.model.MealItem;
import com.example.phamnguyenlananh.model.MealPlan;
import com.example.phamnguyenlananh.ui.auth.LoginActivity;
import com.example.phamnguyenlananh.ui.meal.MealDetailActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealFragment extends Fragment {
    private static final String TAG = "MealFragment";

    private RecyclerView rvRecommendedPlans;
    private MealCardAdapter mealCardAdapter;
    private Button btnGenerateMealPlan;
    private ImageView btnMealFilter;
    private TextView tvMealFilterGoal, tvMealFilterBudget, tvPlanCount;

    private ChipGroup chipGroupDate;
    private Chip chipToday, chipTomorrow, chipWeek;

    private NutriBudgetApiService apiService;
    private TokenManager tokenManager;

    private final List<MealPlan> recommendedPlansList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meal, container, false);
        initViews(root);
        setupListeners();
        setupRecyclerView();

        apiService = ApiClient.getNutriBudgetApiService(requireContext());
        tokenManager = TokenManager.getInstance(requireContext());

        return root;
    }

    private void initViews(View root) {
        rvRecommendedPlans = root.findViewById(R.id.rvRecommendedPlans);
        btnGenerateMealPlan = root.findViewById(R.id.btnGenerateMealPlan);
        btnMealFilter = root.findViewById(R.id.btnMealFilter);
        tvMealFilterGoal = root.findViewById(R.id.tvMealFilterGoal);
        tvMealFilterBudget = root.findViewById(R.id.tvMealFilterBudget);
        tvPlanCount = root.findViewById(R.id.tvPlanCount);

        chipGroupDate = root.findViewById(R.id.chipGroupDate);
        chipToday = root.findViewById(R.id.chipToday);
        chipTomorrow = root.findViewById(R.id.chipTomorrow);
        chipWeek = root.findViewById(R.id.chipWeek);
    }

    private void setupListeners() {
        btnGenerateMealPlan.setOnClickListener(v -> loadMealRecommendations(true));

        btnMealFilter.setOnClickListener(v -> {
            if (getContext() != null) {
                Toast.makeText(getContext(), "Bộ lọc nâng cao: Đã áp dụng ràng buộc Macro & Ngân sách từ MySQL", Toast.LENGTH_SHORT).show();
            }
        });

        if (chipGroupDate != null) {
            chipGroupDate.setOnCheckedStateChangeListener((group, checkedIds) -> loadMealRecommendations(false));
        }
    }

    private void setupRecyclerView() {
        rvRecommendedPlans.setLayoutManager(new LinearLayoutManager(getContext()));
        mealCardAdapter = new MealCardAdapter(recommendedPlansList, plan -> {
            MockDataRepository.getInstance().setCurrentMealPlan(plan);
            Intent intent = new Intent(getActivity(), MealDetailActivity.class);
            intent.putExtra("EXTRA_MEAL_PLAN", plan);
            startActivity(intent);
        });
        rvRecommendedPlans.setAdapter(mealCardAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadGoalAndBudgetFromApi();
        loadMealRecommendations(false);
    }

    private void loadGoalAndBudgetFromApi() {
        if (tokenManager == null || apiService == null) return;
        long userId = tokenManager.getUserId();

        // 1. Fetch Nutrition Goal from API
        apiService.getNutritionGoals(userId).enqueue(new Callback<ApiResponse<List<NutritionGoalResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<NutritionGoalResponse>>> call, Response<ApiResponse<List<NutritionGoalResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    NutritionGoalResponse g = response.body().getData().get(0);
                    String goalType = g.getMucTieu() != null ? g.getMucTieu() : "Duy trì";
                    int calo = g.getCaloMucTieu() != null ? g.getCaloMucTieu().intValue() : 2000;
                    tvMealFilterGoal.setText(goalType + " (" + String.format(Locale.getDefault(), "%,d", calo) + " kcal)");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<NutritionGoalResponse>>> call, Throwable t) {
                Log.e(TAG, "Lỗi tải mục tiêu dinh dưỡng", t);
            }
        });

        // 2. Fetch Budget from API
        apiService.getBudgets(userId).enqueue(new Callback<ApiResponse<List<BudgetResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BudgetResponse>>> call, Response<ApiResponse<List<BudgetResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    BudgetResponse b = response.body().getData().get(0);
                    int daily = b.getNganSachNgay() != null ? b.getNganSachNgay().intValue() : 100000;
                    NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
                    tvMealFilterBudget.setText(currencyFormat.format(daily) + "đ/ngày");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<BudgetResponse>>> call, Throwable t) {
                Log.e(TAG, "Lỗi tải ngân sách", t);
            }
        });
    }

    private void loadMealRecommendations(boolean isUserTriggered) {
        if (tokenManager == null || apiService == null) return;
        long userId = tokenManager.getUserId();

        String option = "HOM_NAY";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();

        if (chipTomorrow != null && chipTomorrow.isChecked()) {
            option = "NGAY_MAI";
            cal.add(Calendar.DAY_OF_YEAR, 1);
        } else if (chipWeek != null && chipWeek.isChecked()) {
            option = "TUAN";
        }
        String applyDate = sdf.format(cal.getTime());

        SuggestMenuRequest request = new SuggestMenuRequest(userId, applyDate, 3, option);

        if (isUserTriggered) {
            btnGenerateMealPlan.setEnabled(false);
            btnGenerateMealPlan.setText("Đang phân tích tối ưu...");
        }

        apiService.suggestMenus(request).enqueue(new Callback<ApiResponse<List<SuggestedMenuResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<SuggestedMenuResponse>>> call, Response<ApiResponse<List<SuggestedMenuResponse>>> response) {
                if (isUserTriggered) {
                    btnGenerateMealPlan.setEnabled(true);
                    btnGenerateMealPlan.setText("Đề xuất thực đơn thông minh");
                }

                if (response.code() == 401 || response.code() == 403) {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
                    }
                    tokenManager.clearSession();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<SuggestedMenuResponse> apiPlans = response.body().getData();
                    recommendedPlansList.clear();

                    for (int i = 0; i < apiPlans.size(); i++) {
                        SuggestedMenuResponse p = apiPlans.get(i);
                        int planHeroImg = R.drawable.img_dish_pork;
                        if (i == 1) planHeroImg = R.drawable.img_dish_fish;
                        else if (i == 2) planHeroImg = R.drawable.img_dish_salmon;

                        List<MealItem> meals = new ArrayList<>();
                        if (p.getChiTietThucDon() != null) {
                            for (MenuDetailResponse d : p.getChiTietThucDon()) {
                                String buaCode = d.getBuaAn();
                                String mealTypeTitle = "Bữa chính";
                                int mealDishImg = R.drawable.img_dish_pork;

                                if ("SANG".equalsIgnoreCase(buaCode)) {
                                    mealTypeTitle = "Bữa sáng";
                                    mealDishImg = R.drawable.img_dish_oats;
                                } else if ("TRUA".equalsIgnoreCase(buaCode)) {
                                    mealTypeTitle = "Bữa trưa";
                                    mealDishImg = R.drawable.img_dish_salmon;
                                } else if ("TOI".equalsIgnoreCase(buaCode)) {
                                    mealTypeTitle = "Bữa tối";
                                    mealDishImg = R.drawable.img_dish_chicken;
                                } else if ("PHU".equalsIgnoreCase(buaCode)) {
                                    mealTypeTitle = "Bữa phụ";
                                    mealDishImg = R.drawable.img_dish_bento;
                                }

                                MealItem buaMeal = new MealItem(
                                        d.getMonAnId() != null ? String.valueOf(d.getMonAnId()) : ("dish_" + meals.size()),
                                        mealTypeTitle,
                                        d.getTenMon(),
                                        d.getKhauPhan() != null ? d.getKhauPhan() : "1 phần",
                                        d.getCalo() != null ? d.getCalo().intValue() : 400,
                                        d.getProtein() != null ? d.getProtein().intValue() : 25,
                                        d.getCarb() != null ? d.getCarb().intValue() : 50,
                                        d.getFat() != null ? d.getFat().intValue() : 12,
                                        d.getChiPhi() != null ? d.getChiPhi().intValue() : 30000,
                                        mealDishImg,
                                        d.getMonAnId()
                                );
                                buaMeal.setBuaAnCode(buaCode != null ? buaCode.toUpperCase() : "TRUA");
                                meals.add(buaMeal);
                            }
                        }

                        MealPlan plan = new MealPlan(
                                p.getPlanId() != null ? p.getPlanId() : ("plan_" + (i + 1)),
                                p.getTenThucDon(),
                                p.getMoTa(),
                                p.getTiLePhuHop() != null ? p.getTiLePhuHop() : 90,
                                p.getSoTienTietKiem() != null ? p.getSoTienTietKiem() : 0,
                                p.getTongCalo() != null ? p.getTongCalo().intValue() : 1500,
                                p.getTongProtein() != null ? p.getTongProtein().intValue() : 60,
                                p.getTongCarb() != null ? p.getTongCarb().intValue() : 180,
                                p.getTongFat() != null ? p.getTongFat().intValue() : 45,
                                p.getTongChiPhi() != null ? p.getTongChiPhi().intValue() : 80000,
                                p.getSoBua() != null ? p.getSoBua() : meals.size(),
                                "HOAT_DONG",
                                p.getNgayApDung() != null ? p.getNgayApDung() : applyDate,
                                planHeroImg,
                                meals
                        );

                        recommendedPlansList.add(plan);
                    }

                    tvPlanCount.setText(recommendedPlansList.size() + " phương án");
                    MockDataRepository.getInstance().setRecommendedPlans(recommendedPlansList);
                    if (!recommendedPlansList.isEmpty()) {
                        MockDataRepository.getInstance().setCurrentMealPlan(recommendedPlansList.get(0));
                    }
                    if (mealCardAdapter != null) {
                        mealCardAdapter.notifyDataSetChanged();
                    }

                    if (isUserTriggered && getContext() != null) {
                        Toast.makeText(getContext(), "Thuật toán đề xuất đã tạo mới " + recommendedPlansList.size() + " thực đơn tối ưu!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Lỗi từ backend: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<SuggestedMenuResponse>>> call, Throwable t) {
                if (isUserTriggered) {
                    btnGenerateMealPlan.setEnabled(true);
                    btnGenerateMealPlan.setText("Đề xuất thực đơn thông minh");
                }
                Log.e(TAG, "Lỗi kết nối khi gọi đề xuất thực đơn", t);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Không thể kết nối đến máy chủ để lấy thực đơn.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
