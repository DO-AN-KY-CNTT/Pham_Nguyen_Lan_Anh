package com.example.phamnguyenlananh.data;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.model.AlternativeDish;
import com.example.phamnguyenlananh.model.BudgetPlan;
import com.example.phamnguyenlananh.model.IngredientItem;
import com.example.phamnguyenlananh.model.MealItem;
import com.example.phamnguyenlananh.model.MealPlan;
import com.example.phamnguyenlananh.model.NutritionGoal;
import com.example.phamnguyenlananh.model.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class MockDataRepository {
    private static MockDataRepository instance;

    private UserProfile userProfile;
    private NutritionGoal nutritionGoal;
    private BudgetPlan budgetPlan;
    private MealPlan currentMealPlan;
    private List<MealPlan> recommendedPlans;
    private List<MealPlan> mealHistory;
    private List<IngredientItem> groceryList;
    private List<AlternativeDish> alternativeDishes;

    private MockDataRepository() {
        initData();
    }

    public static synchronized MockDataRepository getInstance() {
        if (instance == null) {
            instance = new MockDataRepository();
        }
        return instance;
    }

    private void initData() {
        // 1. User Profile
        userProfile = new UserProfile(
                "Nguyễn Văn An",
                "an.nguyen@example.com",
                "0987654321",
                "Nam",
                24,
                175,
                68.0,
                "PRO (Thành viên Tiêu Chuẩn)",
                14,
                420000
        );

        // 2. Nutrition Goal
        nutritionGoal = new NutritionGoal(
                "Tăng cơ",
                2000,
                1650,
                130,
                105,
                220,
                180,
                55,
                42
        );

        // 3. Budget Plan
        budgetPlan = new BudgetPlan(
                100000,
                700000,
                3000000,
                85000
        );

        // 4. Current Meal Plan (Today)
        List<MealItem> currentMeals = new ArrayList<>();
        currentMeals.add(new MealItem(
                "m1",
                "Bữa sáng",
                "Yến mạch & Trứng luộc",
                "1 bát yến mạch (50g) + 2 quả trứng gà",
                420,
                24,
                45,
                12,
                22000,
                R.drawable.img_dish_oats
        ));
        currentMeals.add(new MealItem(
                "m2",
                "Bữa trưa",
                "Ức gà áp chảo sốt chanh",
                "200g ức gà + 1 chén cơm gạo lứt + bông cải",
                680,
                55,
                85,
                18,
                38000,
                R.drawable.img_dish_chicken
        ));
        currentMeals.add(new MealItem(
                "m3",
                "Bữa tối",
                "Cá diêu hồng hấp gừng hành",
                "180g phi lê cá + 1 đĩa rau luộc + canh rong biển",
                550,
                51,
                60,
                15,
                25000,
                R.drawable.img_dish_fish
        ));

        currentMealPlan = new MealPlan(
                "plan_today",
                "Thực đơn Tăng cơ Tiết kiệm #1",
                "Tối ưu hóa nguồn đạm cao cấp từ trứng, ức gà và cá tươi, giúp giữ cơ bền vững với mức ngân sách sinh viên & dân công sở.",
                98,
                15000,
                1650,
                130,
                190,
                45,
                85000,
                3,
                "Đang áp dụng",
                "Hôm nay, 24/10",
                R.drawable.img_dish_chicken,
                currentMeals
        );

        // 5. Recommended Plans
        recommendedPlans = new ArrayList<>();
        recommendedPlans.add(currentMealPlan);

        List<MealItem> plan2Meals = new ArrayList<>();
        plan2Meals.add(new MealItem("p2_1", "Bữa sáng", "Bánh mì đen kẹp trứng", "2 lát bánh mì + 2 quả trứng", 380, 20, 40, 12, 18000, R.drawable.img_dish_oats));
        plan2Meals.add(new MealItem("p2_2", "Bữa trưa", "Cơm tấm thịt nạc luộc", "150g thịt nạc + 1 chén cơm + dưa leo", 620, 45, 80, 14, 32000, R.drawable.img_dish_pork));
        plan2Meals.add(new MealItem("p2_3", "Bữa tối", "Canh đậu hũ rong biển thịt băm", "100g thịt nạc + 1 bìa đậu + rau", 450, 38, 35, 12, 25000, R.drawable.img_dish_vietnamese_lunch));

        recommendedPlans.add(new MealPlan(
                "plan_2",
                "Thực đơn Healthy Sinh viên Cân đối",
                "Dễ chế biến, tiết kiệm thời gian, nguyên liệu phổ thông dễ mua tại chợ dân sinh.",
                95,
                25000,
                1800,
                115,
                210,
                45,
                75000,
                3,
                "Gợi ý phù hợp",
                "Ngày mai, 25/10",
                R.drawable.img_dish_pork,
                plan2Meals
        ));

        List<MealItem> plan3Meals = new ArrayList<>();
        plan3Meals.add(new MealItem("p3_1", "Bữa sáng", "Khoai lang luộc & Sữa đậu nành", "1 củ khoai + 1 ly sữa hạt", 350, 15, 60, 5, 15000, R.drawable.img_dish_bento));
        plan3Meals.add(new MealItem("p3_2", "Bữa trưa", "Cá hồi áp chảo măng tây", "150g cá hồi + rau củ nướng + cơm lứt", 720, 52, 65, 25, 52000, R.drawable.img_dish_salmon));
        plan3Meals.add(new MealItem("p3_3", "Bữa tối", "Salad ức gà xé hạt điều", "150g ức gà + sốt mè rang", 480, 42, 30, 15, 25000, R.drawable.img_dish_chicken));

        recommendedPlans.add(new MealPlan(
                "plan_3",
                "Thực đơn Eat Clean Nhanh Gọn",
                "Giàu Omega-3 và chất xơ, chuẩn vị nhà hàng nhưng kiểm soát calo nghiêm ngặt.",
                91,
                8000,
                1950,
                125,
                195,
                55,
                92000,
                3,
                "Gợi ý chất lượng cao",
                "Cuối tuần",
                R.drawable.img_dish_salmon,
                plan3Meals
        ));

        // 6. History
        mealHistory = new ArrayList<>();
        mealHistory.add(currentMealPlan);
        mealHistory.add(new MealPlan(
                "hist_1",
                "Thực đơn Đạm cao & Rau xanh",
                "Tập trung tăng cơ nhanh với tỷ lệ protein 35%",
                96,
                8000,
                1720,
                135,
                180,
                42,
                92000,
                3,
                "Đã hoàn thành",
                "Hôm qua, 23/10",
                R.drawable.img_dish_pork,
                plan2Meals
        ));
        mealHistory.add(new MealPlan(
                "hist_2",
                "Thực đơn Eat Clean Cá hồi & Bò",
                "Bổ sung vi chất và chất béo tốt",
                94,
                2000,
                1850,
                128,
                200,
                48,
                98000,
                3,
                "Đã hoàn thành",
                "Thứ Ba, 22/10",
                R.drawable.img_dish_salmon,
                plan3Meals
        ));
        mealHistory.add(new MealPlan(
                "hist_3",
                "Thực đơn Tiết kiệm Trứng & Đậu",
                "Ngân sách tối giản cho sinh viên cuối tháng",
                92,
                35000,
                1580,
                100,
                175,
                38,
                65000,
                3,
                "Đã hoàn thành",
                "Thứ Hai, 21/10",
                R.drawable.img_dish_oats,
                plan2Meals
        ));

        // 7. Grocery List
        groceryList = new ArrayList<>();
        groceryList.add(new IngredientItem("ing_1", "Ức gà tươi phi lê", "200g", 18000, "Thịt & Đạm", true));
        groceryList.add(new IngredientItem("ing_2", "Trứng gà ta", "2 quả", 7000, "Thịt & Đạm", true));
        groceryList.add(new IngredientItem("ing_3", "Gạo lứt huyết rồng", "100g", 4000, "Tinh bột", false));
        groceryList.add(new IngredientItem("ing_4", "Cá diêu hồng làm sạch", "200g", 25000, "Thịt & Đạm", false));
        groceryList.add(new IngredientItem("ing_5", "Bông cải xanh & Cà rốt", "250g", 16000, "Rau củ", false));
        groceryList.add(new IngredientItem("ing_6", "Yến mạch cán dẹt", "50g", 6000, "Tinh bột", false));
        groceryList.add(new IngredientItem("ing_7", "Rau cải thìa & Rong biển", "1 phần", 9000, "Rau củ", false));

        // 8. Alternative Dishes for Swap
        alternativeDishes = new ArrayList<>();
        alternativeDishes.add(new AlternativeDish(
                "alt_1",
                "Thịt nạc heo luộc sốt mè",
                490,
                48,
                32000,
                R.drawable.img_dish_pork,
                "Tiết kiệm 6.000đ",
                "Chuẩn dinh dưỡng ±5%"
        ));
        alternativeDishes.add(new AlternativeDish(
                "alt_2",
                "Cá diêu hồng áp chảo thì là",
                480,
                45,
                35000,
                R.drawable.img_dish_fish,
                "Tiết kiệm 3.000đ",
                "Chuẩn dinh dưỡng ±4%"
        ));
        alternativeDishes.add(new AlternativeDish(
                "alt_3",
                "Cơm bento cá hồi sốt teriyaki",
                580,
                42,
                48000,
                R.drawable.img_dish_bento,
                "Thêm 10.000đ",
                "Giàu Omega-3"
        ));
    }

    public UserProfile getUserProfile() { return userProfile; }
    public NutritionGoal getNutritionGoal() { return nutritionGoal; }
    public BudgetPlan getBudgetPlan() { return budgetPlan; }
    public MealPlan getCurrentMealPlan() { return currentMealPlan; }
    public List<MealPlan> getRecommendedPlans() { return recommendedPlans; }
    public List<MealPlan> getMealHistory() { return mealHistory; }
    public List<IngredientItem> getGroceryList() { return groceryList; }
    public List<AlternativeDish> getAlternativeDishes() { return alternativeDishes; }

    public void swapLunchDish(AlternativeDish newDish) {
        if (currentMealPlan != null && currentMealPlan.getMeals() != null && currentMealPlan.getMeals().size() > 1) {
            MealItem lunch = currentMealPlan.getMeals().get(1);
            lunch.setDishName(newDish.getDishName());
            lunch.setCalories(newDish.getCalories());
            lunch.setProtein(newDish.getProtein());
            lunch.setCost(newDish.getCost());
            lunch.setImageRes(newDish.getImageRes());
            lunch.setPortion("Khẩu phần chuẩn cân đối dinh dưỡng");

            currentMealPlan.recalculateTotals();
            budgetPlan.setSpentToday(currentMealPlan.getTotalCost());
            nutritionGoal.setConsumedCalories(currentMealPlan.getTotalCalories());
            nutritionGoal.setConsumedProtein(currentMealPlan.getTotalProtein());
        }
    }

    public void toggleGroceryItem(int position) {
        if (position >= 0 && position < groceryList.size()) {
            IngredientItem item = groceryList.get(position);
            item.setChecked(!item.isChecked());
        }
    }

    public void addGroceryItem(IngredientItem item) {
        groceryList.add(item);
    }

    public int getCheckedGroceryCount() {
        int count = 0;
        for (IngredientItem item : groceryList) {
            if (item.isChecked()) count++;
        }
        return count;
    }

    public int getTotalGroceryCost() {
        int total = 0;
        for (IngredientItem item : groceryList) {
            total += item.getUnitPrice();
        }
        return total;
    }

        public void setCurrentMealPlan(MealPlan currentMealPlan) {
        this.currentMealPlan = currentMealPlan;
    }

    public void setRecommendedPlans(List<MealPlan> recommendedPlans) {
        this.recommendedPlans = recommendedPlans;
    }

    public void saveCurrentPlanToHistory() {
        if (!mealHistory.contains(currentMealPlan)) {
            mealHistory.add(0, currentMealPlan);
        }
    }
}