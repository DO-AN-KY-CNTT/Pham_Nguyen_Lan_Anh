package com.example.phamnguyenlananh.model;

import java.io.Serializable;

public class NutritionGoal implements Serializable {
    private String goalType; // Giảm cân, Tăng cân, Duy trì cân nặng, Tăng cơ
    private int targetCalories;
    private int consumedCalories;
    private int proteinGrams;
    private int consumedProtein;
    private int carbsGrams;
    private int consumedCarbs;
    private int fatGrams;
    private int consumedFat;

    public NutritionGoal(String goalType, int targetCalories, int consumedCalories, int proteinGrams, int consumedProtein, int carbsGrams, int consumedCarbs, int fatGrams, int consumedFat) {
        this.goalType = goalType;
        this.targetCalories = targetCalories;
        this.consumedCalories = consumedCalories;
        this.proteinGrams = proteinGrams;
        this.consumedProtein = consumedProtein;
        this.carbsGrams = carbsGrams;
        this.consumedCarbs = consumedCarbs;
        this.fatGrams = fatGrams;
        this.consumedFat = consumedFat;
    }

    public String getGoalType() { return goalType; }
    public void setGoalType(String goalType) { this.goalType = goalType; }

    public int getTargetCalories() { return targetCalories; }
    public void setTargetCalories(int targetCalories) { this.targetCalories = targetCalories; }

    public int getConsumedCalories() { return consumedCalories; }
    public void setConsumedCalories(int consumedCalories) { this.consumedCalories = consumedCalories; }

    public int getProteinGrams() { return proteinGrams; }
    public void setProteinGrams(int proteinGrams) { this.proteinGrams = proteinGrams; }

    public int getConsumedProtein() { return consumedProtein; }
    public void setConsumedProtein(int consumedProtein) { this.consumedProtein = consumedProtein; }

    public int getCarbsGrams() { return carbsGrams; }
    public void setCarbsGrams(int carbsGrams) { this.carbsGrams = carbsGrams; }

    public int getConsumedCarbs() { return consumedCarbs; }
    public void setConsumedCarbs(int consumedCarbs) { this.consumedCarbs = consumedCarbs; }

    public int getFatGrams() { return fatGrams; }
    public void setFatGrams(int fatGrams) { this.fatGrams = fatGrams; }

    public int getConsumedFat() { return consumedFat; }
    public void setConsumedFat(int consumedFat) { this.consumedFat = consumedFat; }

    public int getRemainingCalories() {
        return Math.max(0, targetCalories - consumedCalories);
    }

    public int getCaloriesPercentage() {
        if (targetCalories <= 0) return 0;
        return (int) Math.min(100, Math.round(((double) consumedCalories / targetCalories) * 100));
    }
}