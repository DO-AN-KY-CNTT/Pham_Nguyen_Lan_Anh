package com.example.phamnguyenlananh.model;

import java.io.Serializable;

public class UserProfile implements Serializable {
    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private int age;
    private int heightCm;
    private double weightKg;
    private String membership;
    private int streakDays;
    private int savedMoney;

    public UserProfile(String fullName, String email, String phone, String gender, int age, int heightCm, double weightKg, String membership, int streakDays, int savedMoney) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.age = age;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.membership = membership;
        this.streakDays = streakDays;
        this.savedMoney = savedMoney;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public int getHeightCm() { return heightCm; }
    public void setHeightCm(int heightCm) { this.heightCm = heightCm; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    public double getBmi() {
        if (heightCm <= 0) return 0;
        double hMeter = heightCm / 100.0;
        return Math.round((weightKg / (hMeter * hMeter)) * 10.0) / 10.0;
    }

    public String getMembership() { return membership; }
    public void setMembership(String membership) { this.membership = membership; }

    public int getStreakDays() { return streakDays; }
    public void setStreakDays(int streakDays) { this.streakDays = streakDays; }

    public int getSavedMoney() { return savedMoney; }
    public void setSavedMoney(int savedMoney) { this.savedMoney = savedMoney; }
}