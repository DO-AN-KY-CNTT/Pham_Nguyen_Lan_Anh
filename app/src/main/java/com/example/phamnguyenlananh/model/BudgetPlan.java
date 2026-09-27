package com.example.phamnguyenlananh.model;

import java.io.Serializable;

public class BudgetPlan implements Serializable {
    private int dailyBudget;
    private int weeklyBudget;
    private int monthlyBudget;
    private int spentToday;

    public BudgetPlan(int dailyBudget, int weeklyBudget, int monthlyBudget, int spentToday) {
        this.dailyBudget = dailyBudget;
        this.weeklyBudget = weeklyBudget;
        this.monthlyBudget = monthlyBudget;
        this.spentToday = spentToday;
    }

    public int getDailyBudget() { return dailyBudget; }
    public void setDailyBudget(int dailyBudget) {
        this.dailyBudget = dailyBudget;
        this.weeklyBudget = dailyBudget * 7;
        this.monthlyBudget = dailyBudget * 30;
    }

    public int getWeeklyBudget() { return weeklyBudget; }
    public void setWeeklyBudget(int weeklyBudget) { this.weeklyBudget = weeklyBudget; }

    public int getMonthlyBudget() { return monthlyBudget; }
    public void setMonthlyBudget(int monthlyBudget) { this.monthlyBudget = monthlyBudget; }

    public int getSpentToday() { return spentToday; }
    public void setSpentToday(int spentToday) { this.spentToday = spentToday; }

    public int getRemainingDailyBudget() {
        return Math.max(0, dailyBudget - spentToday);
    }

    public int getSpentPercentage() {
        if (dailyBudget <= 0) return 0;
        return (int) Math.min(100, Math.round(((double) spentToday / dailyBudget) * 100));
    }
}