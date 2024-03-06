package com.poste.reusables

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.poste.http.LoginRequest
import com.poste.http.RetrofitClient
import com.poste.tokens.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

fun handleLogin(
    email: String,
    password: String,
    context: Context,
    navController: NavController,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        try {
            val response = RetrofitClient.apiService.loginUser(
                LoginRequest(email, password)
            )

            // Now directly handling the response without enqueue
            if (response.isSuccessful) {
                // Parse the response body
                val responseBody = response.body()?.string() ?: ""
                val jsonObject = JSONObject(responseBody)
                val result = jsonObject.getJSONObject("result")
                val token = result.getString("token")

                withContext(Dispatchers.Main) {
                    TokenManager.saveToken(context, token) // Save the token
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    navController.navigate("dashboard") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            } else {
                // Handle login failure
                val errorMessage = when (response.code()) {
                    401 -> "Invalid user credentials; please try again."
                    400 -> "Request missing required fields; please try again."
                    else -> "Unknown error occurred, please try again later."
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            // Handle exceptions like network errors
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Login failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


