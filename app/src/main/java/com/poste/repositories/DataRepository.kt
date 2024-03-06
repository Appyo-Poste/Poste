package com.poste.repositories

import android.content.Context
import com.poste.http.RetrofitClient
import com.poste.models.ApiResponse
import com.poste.tokens.TokenManager
import kotlinx.coroutines.flow.first
import retrofit2.Response

object DataRepository {
    suspend fun fetchData(context: Context): ApiResponse {
        val token = TokenManager.getTokenFlow(context).first() ?: throw IllegalStateException("Token not found")
        val response: Response<ApiResponse> = RetrofitClient.apiService.fetchData("Token $token")

        if (response.isSuccessful) {
            return response.body() ?: throw IllegalStateException("No data available")
        } else {
            throw Exception("API call failed with error code: ${response.code()} and message: ${response.errorBody()?.string()}")
        }
    }
}