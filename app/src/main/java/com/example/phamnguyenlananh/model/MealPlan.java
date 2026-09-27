package com.example.phamnguyenlananh.model;

import java.io.Serializable;
import java.util.List;

public class MealPlan implements Serializable {
    private String id;
    private String title;
    private String subtitle;
    private int matchRate;
    private int savingsAmount;
    private int totalCalories;
    private int totalProtein;
    private int totalCarbs;
    private int totalFat;
    private int totalCost;
    private int mealCount;
    private String status;
    private String date;
    private int imageRes;
    private List<MealItem> meals;

    public MealPlan(String id, String title, String subtitle, int matchRate, int savingsAmount, int totalCalories, int totalProtein, int totalCarbs, int totalFat, int totalCost, int mealCount, String status, String date, int imageRes, List<MealItem> meals) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.matchRate = matchRate;
        this.savingsAmount = savingsAmount;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalCarbs = totalCarbs;
        this.totalFat = totalFat;
        this.totalCost = totalCost;
        this.mealCount = mealCount;
        this.status = status;
        this.date = date;
        this.imageRes = imageRes;
        this.meals = meals;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public int getMatchRate() { return matchRate; }
    public int getSavingsAmount() { return savingsAmount; }
    public int getTotalCalories() { return totalCalories; }
    public void setTotalCalories(int totalCalories) { this.totalCalories = totalCalories; }
    public int getTotalProtein() { return totalProtein; }
    public void setTotalProtein(int totalProtein) { this.totalProtein = totalProtein; }
    public int getTotalCarbs() { return totalCarbs; }
    public void setTotalCarbs(int totalCarbs) { this.totalCarbs = totalCarbs; }
    public int getTotalFat() { return totalFat; }
    public void setTotalFat(int totalFat) { this.totalFat = totalFat; }
    public int getTotalCost() { return totalCost; }
    public void setTotalCost(int totalCost) { this.totalCost = totalCost; }
    public int getMealCount() { return mealCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public int getImageRes() { return imageRes; }
    public List<MealItem> getMeals() { return meals; }
    public void setMeals(List<MealItem> meals) { this.meals = meals; }

    public void recalculateTotals() {
        if (meals == null) return;
        int cal = 0, p = 0, c = 0, f = 0, cost = 0;
        for (MealItem item : meals) {
            cal += item.getCalories();
            p += item.getProtein();
            c += item.getCarbs();
            f += item.getFat();
            cost += item.getCost();
        }
        this.totalCalories = cal;
        this.totalProtein = p;
        this.totalCarbs = c;
        this.totalFat = f;
        this.totalCost = cost;
    }
}