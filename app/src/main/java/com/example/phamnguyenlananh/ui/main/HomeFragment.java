package com.example.phamnguyenlananh.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phamnguyenlananh.MainActivity;
import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.adapter.MealDetailSectionAdapter;
import com.example.phamnguyenlananh.data.MockDataRepository;
import com.example.phamnguyenlananh.model.BudgetPlan;
import com.example.phamnguyenlananh.model.MealPlan;
import com.example.phamnguyenlananh.model.NutritionGoal;
import com.example.phamnguyenlananh.ui.meal.CostOverviewActivity;
import com.example.phamnguyenlananh.ui.meal.GroceryListActivity;
import com.example.phamnguyenlananh.ui.meal.MealDetailActivity;
import com.example.phamnguyenlananh.ui.profile.NutritionGoalsActivity;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.NumberFormat;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private TextView tvGreeting, tvDate, tvCaloriePercent, tvHomeCalorieRatio, tvCalorieRemain;
    private TextView tvHomeBudgetRatio, tvHomeBudgetSurplus, tvHomeSpentToday, tvHomeDailyLimit;
    private TextView tvHomeProtein, tvHomeCarbs, tvHomeFat, tvViewAllMeals;
    private CircularProgressIndicator progressCalorieCircle;
    private LinearProgressIndicator progressBudgetLine;
    private RecyclerView rvHomeMeals;
    private MealDetailSectionAdapter mealsAdapter;
    private ImageView btnNotification, imgHomeAvatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(root);
        setupListeners(root);
        setupRecyclerView();
        updateUI();
        return root;
    }

    private void initViews(View root) {
        tvGreeting = root.findViewById(R.id.tvGreeting);
        tvDate = root.findViewById(R.id.tvDate);
        tvCaloriePercent = root.findViewById(R.id.tvCaloriePercent);
        tvHomeCalorieRatio = root.findViewById(R.id.tvHomeCalorieRatio);
        tvCalorieRemain = root.findViewById(R.id.tvCalorieRemain);
        tvHomeBudgetRatio = root.findViewById(R.id.tvHomeBudgetRatio);
        tvHomeBudgetSurplus = root.findViewById(R.id.tvHomeBudgetSurplus);
        tvHomeSpentToday = root.findViewById(R.id.tvHomeSpentToday);
        tvHomeDailyLimit = root.findViewById(R.id.tvHomeDailyLimit);
        tvHomeProtein = root.findViewById(R.id.tvHomeProtein);
        tvHomeCarbs = root.findViewById(R.id.tvHomeCarbs);
        tvHomeFat = root.findViewById(R.id.tvHomeFat);
        tvViewAllMeals = root.findViewById(R.id.tvViewAllMeals);
        progressCalorieCircle = root.findViewById(R.id.progressCalorieCircle);
        progressBudgetLine = root.findViewById(R.id.progressBudgetLine);
        rvHomeMeals = root.findViewById(R.id.rvHomeMeals);
        btnNotification = root.findViewById(R.id.btnNotification);
        imgHomeAvatar = root.findViewById(R.id.imgHomeAvatar);
    }

    private void setupListeners(View root) {
        // Quick Action 1: Meal Plan
        root.findViewById(R.id.cardQuickMealPlan).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MealDetailActivity.class);
            startActivity(intent);
        });

        // Quick Action 2: Grocery List
        root.findViewById(R.id.cardQuickGrocery).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GroceryListActivity.class);
            startActivity(intent);
        });

        // Quick Action 3: Cost Overview
        root.findViewById(R.id.cardQuickCost).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CostOverviewActivity.class);
            startActivity(intent);
        });

        // Quick Action 4: Goals & Settings
        root.findViewById(R.id.cardQuickSettings).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NutritionGoalsActivity.class);
            startActivity(intent);
        });

        tvViewAllMeals.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MealDetailActivity.class);
            startActivity(intent);
        });

        btnNotification.setOnClickListener(v -> Toast.makeText(getContext(), "Không có thông báo mới.", Toast.LENGTH_SHORT).show());

        imgHomeAvatar.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToTab(R.id.navigation_profile);
            }
        });
    }

    private void setupRecyclerView() {
        rvHomeMeals.setLayoutManager(new LinearLayoutManager(getContext()));
        MealPlan currentPlan = MockDataRepository.getInstance().getCurrentMealPlan();
        if (currentPlan != null) {
            mealsAdapter = new MealDetailSectionAdapter(currentPlan.getMeals());
            rvHomeMeals.setAdapter(mealsAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        MockDataRepository repo = MockDataRepository.getInstance();
        NutritionGoal goal = repo.getNutritionGoal();
        BudgetPlan budget = repo.getBudgetPlan();
        MealPlan plan = repo.getCurrentMealPlan();

        tvGreeting.setText("Chào buổi sáng, " + repo.getUserProfile().getFullName().split(" ")[repo.getUserProfile().getFullName().split(" ").length - 1] + " 👋");

        // Calorie
        int percent = goal.getCaloriesPercentage();
        progressCalorieCircle.setProgress(percent);
        tvCaloriePercent.setText(percent + "%");
        tvHomeCalorieRatio.setText(String.format(Locale.getDefault(), "%,d / %,d", goal.getConsumedCalories(), goal.getTargetCalories()));
        tvCalorieRemain.setText("⚡ Còn " + goal.getRemainingCalories() + " kcal");

        // Budget
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        int spentK = budget.getSpentToday() / 1000;
        int dailyK = budget.getDailyBudget() / 1000;
        tvHomeBudgetRatio.setText(spentK + "k / " + dailyK + "k");
        tvHomeSpentToday.setText("Đã tiêu: " + currencyFormat.format(budget.getSpentToday()) + "đ");
        tvHomeDailyLimit.setText("Hạn mức: " + currencyFormat.format(budget.getDailyBudget()) + "đ");
        progressBudgetLine.setProgress(budget.getSpentPercentage());

        int surplus = budget.getRemainingDailyBudget();
        if (surplus >= 0) {
            tvHomeBudgetSurplus.setText("Dư " + currencyFormat.format(surplus) + "đ");
        } else {
            tvHomeBudgetSurplus.setText("Vượt " + currencyFormat.format(Math.abs(surplus)) + "đ");
        }

        // Macros
        tvHomeProtein.setText(goal.getConsumedProtein() + "/" + goal.getProteinGrams() + "g");
        tvHomeCarbs.setText(goal.getConsumedCarbs() + "/" + goal.getCarbsGrams() + "g");
        tvHomeFat.setText(goal.getConsumedFat() + "/" + goal.getFatGrams() + "g");

        // Meals list
        if (mealsAdapter != null && plan != null) {
            mealsAdapter.updateData(plan.getMeals());
        }
    }
}