package com.poste.reusables

import android.content.Context
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
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        RegisterContent(navController)
    }

}

@Preview
    (showBackground = true)
@Composable
fun PreviewRegisterContent() {
    RegisterContent(navController = rememberNavController())
}

@Composable
fun RegisterContent(navController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

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
                        navController = navController,
                        coroutineScope = coroutineScope
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
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        try {
            val response = RetrofitClient.apiService.registerUser(
                RegisterRequest(
                    email = email,
                    first_name = firstName,
                    last_name = lastName,
                    password = password
                )
            )

            if (response.isSuccessful) {
                // Process successful response
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                    navController.navigate("intro") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            } else {
                // Process error response
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            // Process exception, e.g., network error
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Registration failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}