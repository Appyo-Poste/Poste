package com.poste.http

import com.poste.models.ApiResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface PosteAPIService {
    @POST("users/")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<ResponseBody>

    @POST("login/")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<ResponseBody>

    @GET("data/")
    suspend fun fetchData(@Header("Authorization") token: String): Response<ApiResponse>
}
