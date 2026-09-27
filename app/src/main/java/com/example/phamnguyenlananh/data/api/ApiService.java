package com.example.phamnguyenlananh.data.api;

import com.example.phamnguyenlananh.data.api.model.ApiResponse;
import com.example.phamnguyenlananh.data.api.model.LoginRequest;
import com.example.phamnguyenlananh.data.api.model.LoginResponse;
import com.example.phamnguyenlananh.data.api.model.RegisterRequest;
import com.example.phamnguyenlananh.data.api.model.RegisterResponse;
import com.example.phamnguyenlananh.data.api.model.UpdateUserRequest;
import com.example.phamnguyenlananh.data.api.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @GET("api/users/profile")
    Call<ApiResponse<UserResponse>> getProfile(@Header("Authorization") String authHeader);

    @PUT("api/users/{id}")
    Call<ApiResponse<UserResponse>> updateUser(
            @Header("Authorization") String authHeader,
            @Path("id") Long id,
            @Body UpdateUserRequest request
    );

    @GET("api/users/{id}")
    Call<ApiResponse<UserResponse>> getUserById(
            @Header("Authorization") String authHeader,
            @Path("id") Long id
    );
}
