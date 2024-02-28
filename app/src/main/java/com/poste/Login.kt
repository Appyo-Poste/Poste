package com.poste

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.poste.http.LoginRequest
import com.poste.http.RetrofitClient
import com.poste.reusables.EntryBox
import com.poste.reusables.validateEmail
import com.poste.reusables.validatePassword
import com.poste.tokens.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


@Composable
fun LoginContent(sharedViewModel: SharedViewModel, navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    BackHandler {
        sharedViewModel.currentScreenState.value = ScreenState.INTRO
    }

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "User Login",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        EntryBox("Email", email, { email = it }) { validateEmail(it) }
        Spacer(modifier = Modifier.height(16.dp))
        EntryBox("Password", password, { password = it }, isPassword = true) { validatePassword(password) }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                handleLogin(email, password, context, navController, coroutineScope)
            },
            enabled = validateEmail(email) == null && validatePassword(password) == null,
            modifier = Modifier
                .fillMaxWidth(.6f)
        ) {
            Text(text = "Login")
        }
    }
}

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
                val result = jsonObject.getJSONObject("result") // Adjust based on your actual JSON response structure
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


