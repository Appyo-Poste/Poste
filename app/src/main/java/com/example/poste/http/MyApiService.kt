package com.example.poste.http

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MyApiService {
    @POST("users/")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<ResponseBody>
}
