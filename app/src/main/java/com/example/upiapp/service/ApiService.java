package com.example.upiapp.service;

import com.example.upiapp.models.SignupRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/auth/signup")
    Call<Void> signup(@Body SignupRequest request);
}
