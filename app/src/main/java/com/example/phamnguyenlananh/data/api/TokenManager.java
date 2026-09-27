package com.example.phamnguyenlananh.data.api;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREF_NAME = "NutriBudgetPrefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_FULL_NAME = "user_full_name";

    private static TokenManager instance;
    private final SharedPreferences prefs;

    private TokenManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context);
        }
        return instance;
    }

    public void saveSession(String token, Long userId, String email, String fullName) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putLong(KEY_USER_ID, userId != null ? userId : -1L)
                .putString(KEY_EMAIL, email)
                .putString(KEY_FULL_NAME, fullName)
                .apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public long getUserId() {
        return prefs.getLong(KEY_USER_ID, 1L);
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "lananh@nutribudget.com");
    }

    public String getFullName() {
        return prefs.getString(KEY_FULL_NAME, "Pham Nguyen Lan Anh");
    }

    public boolean isLoggedIn() {
        return getToken() != null && !getToken().isEmpty();
    }

    public void clear() {
        prefs.edit().clear().apply();
    }

    public void clearSession() {
        clear();
    }
}
