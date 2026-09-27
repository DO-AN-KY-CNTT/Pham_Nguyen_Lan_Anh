package com.example.phamnguyenlananh.data.api;

import com.example.phamnguyenlananh.data.api.model.*;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface NutriBudgetApiService {

    // Auth
    @POST("api/auth/register")
    Call<ApiResponse<AuthResponse>> register(@Body RegisterRequest request);

    @POST("api/auth/login")
    Call<ApiResponse<AuthResponse>> login(@Body LoginRequest request);

    // Users
    @GET("api/users/{id}")
    Call<ApiResponse<UserResponse>> getUserById(@Path("id") long id);

    @PUT("api/users/{id}")
    Call<ApiResponse<UserResponse>> updateUser(@Path("id") long id, @Body UpdateUserRequest request);

    @GET("api/users/profile")
    Call<ApiResponse<UserResponse>> getProfile();

    // Nutrition Goals
    @GET("api/nutrition-goals/user/{userId}")
    Call<ApiResponse<List<NutritionGoalResponse>>> getNutritionGoals(@Path("userId") long userId);

    @POST("api/nutrition-goals")
    Call<ApiResponse<NutritionGoalResponse>> createNutritionGoal(@Body NutritionGoalRequest request);

    @PUT("api/nutrition-goals/{id}")
    Call<ApiResponse<NutritionGoalResponse>> updateNutritionGoal(@Path("id") long id, @Body NutritionGoalRequest request);

    // Budgets
    @GET("api/budgets/user/{userId}")
    Call<ApiResponse<List<BudgetResponse>>> getBudgets(@Path("userId") long userId);

    @POST("api/budgets")
    Call<ApiResponse<BudgetResponse>> createBudget(@Body BudgetRequest request);

    @PUT("api/budgets/{id}")
    Call<ApiResponse<BudgetResponse>> updateBudget(@Path("id") long id, @Body BudgetRequest request);

    // Dishes
    @GET("api/dishes")
    Call<ApiResponse<List<DishResponse>>> getDishes(@Query("keyword") String keyword);

    @GET("api/dishes/{id}")
    Call<ApiResponse<DishResponse>> getDishById(@Path("id") long id);

    // Menus
    @GET("api/menus/user/{userId}")
    Call<ApiResponse<List<MenuResponse>>> getMenus(@Path("userId") long userId);

    @GET("api/menus/{id}")
    Call<ApiResponse<MenuResponse>> getMenuById(@Path("id") long id);

    @GET("api/menus/{menuId}/cost")
    Call<ApiResponse<MenuCostResponse>> getMenuCost(@Path("menuId") long menuId);


    @POST("api/menus")
    Call<ApiResponse<MenuResponse>> createMenu(@Body MenuRequest request);

    @PUT("api/menus/{id}")
    Call<ApiResponse<MenuResponse>> updateMenu(@Path("id") long id, @Body MenuRequest request);

    @PUT("api/menus/{menuId}/items/{itemId}")
    Call<ApiResponse<MenuResponse>> updateMenuItem(
            @Path("menuId") long menuId,
            @Path("itemId") long itemId,
            @Body UpdateMenuItemRequest request
    );


    @POST("api/menus/suggest")
    Call<ApiResponse<List<SuggestedMenuResponse>>> suggestMenus(@Body SuggestMenuRequest request);

    // Menu History
    @GET("api/menu-history/user/{userId}")
    Call<ApiResponse<List<MenuHistoryResponse>>> getMenuHistory(@Path("userId") long userId);

    // Menu Ingredients / Grocery List
    @GET("api/menus/{menuId}/ingredients")
    Call<ApiResponse<MenuIngredientsResponse>> getMenuIngredients(@Path("menuId") long menuId);
}
