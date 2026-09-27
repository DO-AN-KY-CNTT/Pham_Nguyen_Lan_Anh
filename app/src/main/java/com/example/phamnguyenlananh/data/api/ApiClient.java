package com.example.phamnguyenlananh.data.api;

import android.content.Context;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static ApiService apiService = null;
    private static NutriBudgetApiService nutriBudgetApiService = null;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConfig.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static synchronized ApiService getApiService() {
        if (apiService == null) {
            apiService = getRetrofit().create(ApiService.class);
        }
        return apiService;
    }

    public static synchronized ApiService getApiService(Context context) {
        return getApiService();
    }

    public static synchronized NutriBudgetApiService getNutriBudgetApiService(Context context) {
        if (nutriBudgetApiService == null) {
            final TokenManager tokenManager = TokenManager.getInstance(context);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request.Builder builder = original.newBuilder();

                            String token = tokenManager.getToken();
                            if (token != null && !token.isEmpty()) {
                                builder.header("Authorization", "Bearer " + token);
                            }
                            builder.header("Accept", "application/json");

                            return chain.proceed(builder.build());
                        }
                    })
                    .addInterceptor(logging)
                    .build();

            Retrofit r = new Retrofit.Builder()
                    .baseUrl(ApiConfig.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            nutriBudgetApiService = r.create(NutriBudgetApiService.class);
        }
        return nutriBudgetApiService;
    }
}