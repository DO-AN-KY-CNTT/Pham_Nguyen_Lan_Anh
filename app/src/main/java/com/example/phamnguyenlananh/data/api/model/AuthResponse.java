package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("tokenType")
    private String tokenType;

    @SerializedName("user")
    private UserResponse user;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public UserResponse getUser() { return user; }
    public void setUser(UserResponse user) { this.user = user; }
}