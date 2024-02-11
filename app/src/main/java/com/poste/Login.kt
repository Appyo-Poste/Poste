package com.poste

import android.content.Context
import android.util.Log
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
import okhttp3.ResponseBody
import retrofit2.Call


@Composable
fun LoginContent(sharedViewModel: SharedViewModel, navController: NavController) {
    val context = LocalContext.current
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
                handleLogin(email, password, context, navController)
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
    navController: NavController
) {
    val call: Call<ResponseBody> = RetrofitClient.instance.loginUser(
        loginRequest = LoginRequest(
            email = email,
            password = password
        )
    )
    call.enqueue(
        object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Log.d("LoginActivity", "onResponse: Success")
                    Toast.makeText(
                        context,
                        "Login successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate("dashboard") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                } else {
                    val errorMessage = when {
                        response.code() == 401 -> "Invalid user credentials; please try again."
                        response.code() == 400 -> "Request missing required fields; please try again."
                        else -> {
                            "Unknown error occurred, please try again later."
                        }
                    }
                    Log.d("LoginActivity", errorMessage)
                    Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = "Unable to reach Poste server; please try again later."
                Log.d("RegisterActivity", message)
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )
}
