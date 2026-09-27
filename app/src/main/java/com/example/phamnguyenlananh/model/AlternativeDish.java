package com.example.phamnguyenlananh.model;

import java.io.Serializable;

public class AlternativeDish implements Serializable {
    private String id;
    private Long dishId;
    private String dishName;
    private int calories;
    private int protein;
    private int carb;
    private int fat;
    private int cost;
    private int imageRes;
    private String savingsOrExtra;
    private String matchTag;
    private String buaAn;

    public AlternativeDish() {}

    public AlternativeDish(String id, String dishName, int calories, int protein, int cost, int imageRes, String savingsOrExtra, String matchTag) {
        this.id = id;
        this.dishName = dishName;
        this.calories = calories;
        this.protein = protein;
        this.cost = cost;
        this.imageRes = imageRes;
        this.savingsOrExtra = savingsOrExtra;
        this.matchTag = matchTag;
        try {
            this.dishId = Long.parseLong(id);
        } catch (Exception ignored) {}
    }

    public AlternativeDish(Long dishId, String dishName, int calories, int protein, int carb, int fat, int cost, int imageRes, String savingsOrExtra, String matchTag, String buaAn) {
        this.dishId = dishId;
        this.id = dishId != null ? String.valueOf(dishId) : "0";
        this.dishName = dishName;
        this.calories = calories;
        this.protein = protein;
        this.carb = carb;
        this.fat = fat;
        this.cost = cost;
        this.imageRes = imageRes;
        this.savingsOrExtra = savingsOrExtra;
        this.matchTag = matchTag;
        this.buaAn = buaAn;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }

    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public int getProtein() { return protein; }
    public void setProtein(int protein) { this.protein = protein; }

    public int getCarb() { return carb; }
    public void setCarb(int carb) { this.carb = carb; }

    public int getFat() { return fat; }
    public void setFat(int fat) { this.fat = fat; }

    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }

    public int getImageRes() { return imageRes; }
    public void setImageRes(int imageRes) { this.imageRes = imageRes; }

    public String getSavingsOrExtra() { return savingsOrExtra; }
    public void setSavingsOrExtra(String savingsOrExtra) { this.savingsOrExtra = savingsOrExtra; }

    public String getMatchTag() { return matchTag; }
    public void setMatchTag(String matchTag) { this.matchTag = matchTag; }

    public String getBuaAn() { return buaAn; }
    public void setBuaAn(String buaAn) { this.buaAn = buaAn; }
}
