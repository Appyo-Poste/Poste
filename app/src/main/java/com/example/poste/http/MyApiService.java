package com.example.poste.http;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import okhttp3.ResponseBody;

public interface MyApiService {
    @POST("users/")
    Call<ResponseBody> registerUser(@Body RegisterRequest registerRequest);

    @POST("login/")
    Call<ResponseBody> loginUser(@Body LoginRequest loginRequest);

}
