package com.example.phamnguyenlananh.model;

import java.io.Serializable;

public class MealItem implements Serializable {
    private String id;
    private String mealType;    // Display title
    private String buaAnCode;   // Raw code: SANG, TRUA, TOI, PHU
    private String dishName;
    private String portion;
    private int calories;
    private int protein;
    private int carbs;
    private int fat;
    private int cost;
    private int imageRes;
    private Long dishId;
    private Long detailId;

    public MealItem(String id, String mealType, String dishName, String portion, int calories, int protein, int carbs, int fat, int cost, int imageRes) {
        this.id = id;
        this.mealType = mealType;
        this.buaAnCode = null;
        this.dishName = dishName;
        this.portion = portion;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.cost = cost;
        this.imageRes = imageRes;
    }

    public MealItem(String id, String mealType, String dishName, String portion, int calories, int protein, int carbs, int fat, int cost, int imageRes, Long dishId) {
        this(id, mealType, dishName, portion, calories, protein, carbs, fat, cost, imageRes);
        this.dishId = dishId;
    }

    public MealItem(String id, String mealType, String dishName, String portion, int calories, int protein, int carbs, int fat, int cost, int imageRes, Long dishId, Long detailId) {
        this(id, mealType, dishName, portion, calories, protein, carbs, fat, cost, imageRes, dishId);
        this.detailId = detailId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public String getBuaAnCode() { return buaAnCode; }
    public void setBuaAnCode(String buaAnCode) { this.buaAnCode = buaAnCode; }
    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }
    public String getPortion() { return portion; }
    public void setPortion(String portion) { this.portion = portion; }
    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }
    public int getProtein() { return protein; }
    public void setProtein(int protein) { this.protein = protein; }
    public int getCarbs() { return carbs; }
    public void setCarbs(int carbs) { this.carbs = carbs; }
    public int getFat() { return fat; }
    public void setFat(int fat) { this.fat = fat; }
    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }
    public int getImageRes() { return imageRes; }
    public void setImageRes(int imageRes) { this.imageRes = imageRes; }
    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }
    public Long getDetailId() { return detailId; }
    public void setDetailId(Long detailId) { this.detailId = detailId; }
}
