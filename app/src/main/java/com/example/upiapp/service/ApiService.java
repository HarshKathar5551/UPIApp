package com.example.upiapp.service;

import com.example.upiapp.models.LoginRequest;
import com.example.upiapp.models.LoginResponse;
import com.example.upiapp.models.SetPinRequest;
import com.example.upiapp.models.SignupRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/auth/signup")
    Call<Void> signup(@Body SignupRequest request);
    @POST("/auth/login") // Target your Flask /auth/login endpoint
    Call<LoginResponse> login(@Body LoginRequest request);
    @POST("/auth/set-pin") //
    Call<Void> setPin(@Body SetPinRequest request);
}
