package com.example.poste

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.poste.http.RegisterRequest
import com.example.poste.http.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call

class IntroActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Poste()
        }
    }
}

@Composable
fun Poste() {
    val navController = rememberNavController()
    val sharedViewModel = remember { SharedViewModel() }
    NavHost(navController = navController, startDestination = "dynamic") {
        composable("dynamic") { DynamicScreen(sharedViewModel) }
    }
}

@Composable
fun DynamicScreen(sharedViewModel: SharedViewModel) {
    val currentState = sharedViewModel.currentScreenState.value
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_intro),
            contentDescription = "Intro Logo",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
        )
        AnimatedVisibility(visible = currentState == ScreenState.INTRO) {
            IntroContent(sharedViewModel = sharedViewModel)
        }
        AnimatedVisibility(visible = currentState == ScreenState.REGISTER ) {
            RegisterContent(sharedViewModel = sharedViewModel)
        }

    }
}

@Composable
fun IntroContent(sharedViewModel: SharedViewModel) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                sharedViewModel.currentScreenState.value = ScreenState.REGISTER
            },
            modifier = Modifier
                .fillMaxWidth(.6f)
        ) {
            Text(text = "Create Account")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                //TODO: Handle login navigation
            },
            modifier = Modifier
                .fillMaxWidth(.6f)
        ) {
            Text(text = "Login")
        }
    }

}

enum class RegistrationStep { Email, FirstName, LastName, Password, ConfirmPassword }


@Composable
fun RegisterContent(sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    var currentStep by remember { mutableStateOf(RegistrationStep.Email) }
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val progress = currentStep.ordinal / (RegistrationStep.entries.size - 1).toFloat()
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Account Registration",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        LinearProgressIndicator(
            progress = animatedProgress.value,
            modifier = Modifier
                .fillMaxWidth(.8f)
        )
        when (currentStep) {
            RegistrationStep.Email -> {
                Text(
                    text = "First, we'll need your email",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.height(16.dp))
                EntryBox("Email", email, { email = it })
            }
            RegistrationStep.FirstName ->
            {
                Text(
                    text = "Next, we'll need your first name",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.height(16.dp))
                EntryBox("First Name", firstName, { firstName = it })
            }
            RegistrationStep.LastName -> {
                Text(
                    text = "Hi, $firstName. What's your last name?",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.height(16.dp))
                EntryBox("Last Name", lastName, { lastName = it })
            }
            RegistrationStep.Password -> {
                Text(
                    text = "Got it. $firstName $lastName. Go ahead and set your password",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                )
                EntryBox(
                    "Password",
                    password,
                    { password = it },
                    isPassword = true
                )
            }
            RegistrationStep.ConfirmPassword -> {
                Text(
                    text = "One more time -- confirm your password",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.height(16.dp))
                EntryBox(
                    "Confirm Password",
                    confirmPassword,
                    { confirmPassword = it },
                    isPassword = true
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                when (currentStep) {
                    RegistrationStep.ConfirmPassword -> {
                        //TODO: Handle registration
                        handleRegistration(
                            email = email,
                            firstName = firstName,
                            lastName = lastName,
                            password = password,
                            context = context,
                            sharedViewModel = sharedViewModel
                        )
                    }

                    else -> {
                        val nextOrdinal = currentStep.ordinal + 1
                        currentStep = RegistrationStep.entries.toTypedArray()[nextOrdinal]
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth(.6f)
        ) {
            Text(text = if (currentStep == RegistrationStep.ConfirmPassword) "Register" else "Next")
        }
    }
}

fun handleRegistration(email: String, firstName: String, lastName: String, password: String, context: Context, sharedViewModel: SharedViewModel) {
    var call: Call<ResponseBody> = RetrofitClient.instance.registerUser(
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
                    sharedViewModel.currentScreenState.value = ScreenState.INTRO
                } else {
                    val message: String = "onResponse: !isSuccessful"
                    Log.d("RegisterActivity", message)
                    Toast.makeText(
                        context,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message: String = "onFailure: ${t.message}"
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

@Composable
fun EntryBox(label: String, text: String, onTextChange: (String) -> Unit, isPassword: Boolean = false) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text(text = label) },
        modifier = Modifier.fillMaxWidth(.8f),
        shape = RoundedCornerShape(16.dp),
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }
    )
}