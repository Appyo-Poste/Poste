package com.poste.http

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PosteAPIService {
    @POST("users/")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<ResponseBody>
    @POST("login/")
    fun loginUser(@Body loginRequest: LoginRequest): Call<ResponseBody>
}
