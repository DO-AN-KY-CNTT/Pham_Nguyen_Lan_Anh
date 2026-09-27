package com.example.phamnguyenlananh.data.api;

public interface ApiCallback<T> {
    void onSuccess(T data);
    void onError(String errorMessage);
}