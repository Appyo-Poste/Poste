package com.poste

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.poste.http.RegisterRequest
import com.poste.http.RetrofitClient
import com.poste.reusables.BrokenDividerWithText
import com.poste.reusables.EntryBox
import com.poste.reusables.rememberImeState
import com.poste.reusables.validateConfirmPassword
import com.poste.reusables.validateEmail
import com.poste.reusables.validateName
import com.poste.reusables.validatePassword
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import java.util.Locale

@Composable
fun RegisterScreen(navController: NavHostController) {
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
        }
    }
    Surface(color = MaterialTheme.colorScheme.background) {
        RegisterContent2(navController)
    }

}

@Preview
@Composable
fun PreviewRegisterContent() {
    RegisterContent2(navController = rememberNavController())
}

@Composable
fun RegisterContent2(navController: NavHostController) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo((scrollState.maxValue * .5).toInt(), tween(300))
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Create account",
            fontSize = 32.sp,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Start),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Welcome to Poste!",
            fontSize = 16.sp,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Start),
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(32.dp))
        EntryBox("Email", email, { email = it }) { validateEmail(it) }
        Spacer(modifier = Modifier.height(16.dp))
        EntryBox("First Name", firstName, { firstName = it }) { validateName(it) }
        Spacer(modifier = Modifier.height(16.dp))
        EntryBox("Last Name", lastName, { lastName = it }) { validateName(it) }
        Spacer(modifier = Modifier.height(16.dp))
        EntryBox(
            "Password",
            password,
            { password = it },
            isPassword = true
        ) { validatePassword(it) }
        Spacer(modifier = Modifier.height(16.dp))
        EntryBox(
            "Confirm Password",
            confirmPassword,
            { confirmPassword = it },
            isPassword = true
        ) { validateConfirmPassword(password, it) }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (validateConfirmPassword(password, confirmPassword) == null) {
                    handleRegistration(
                        email = email,
                        firstName = firstName,
                        lastName = lastName,
                        password = password,
                        context = context,
                        navController = navController
                    )
                }
            },
            enabled = (
                    validateEmail(email) == null
                            && validateName(firstName) == null
                            && validateName(lastName) == null
                            && validatePassword(password) == null
                            && validateConfirmPassword(password, confirmPassword) == null
                    ),
            modifier = Modifier
                .fillMaxWidth(.6f)
        ) {
            Text("Let's get started")
        }
        Spacer(modifier = Modifier.height(16.dp))
        BrokenDividerWithText(text = "or")
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(.85f),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Have an account?",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Log In",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable {
                        navController.navigate("intro")
                    }
            )

        }
    }
}


fun handleRegistration(
    email: String,
    firstName: String,
    lastName: String,
    password: String,
    context: Context,
    navController: NavHostController,
) {
    val call: Call<ResponseBody> = RetrofitClient.instance.registerUser(
        registerRequest = RegisterRequest(
            email = email,
            first_name = firstName,
            last_name = lastName,
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
                    Log.d("RegisterActivity", "onResponse: Success")
                    Toast.makeText(
                        context,
                        "Registration successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate("dashboard") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()?.let { responseBodyString ->
                        try {
                            val jsonObject = JSONObject(responseBodyString)
                            val errors =
                                jsonObject.optJSONArray("email") ?: return@let "Unknown error"
                            if (errors.length() > 0) {
                                errors.optString(0).replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.getDefault()
                                    ) else it.toString()
                                }
                            } else {
                                "Unknown error"
                            }
                        } catch (e: JSONException) {
                            "Error parsing error message"
                        }
                    } ?: "Error occurred"
                    Log.d("RegisterActivity", errorMessage)
                    Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = "onFailure: ${t.message}"
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