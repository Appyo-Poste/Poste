package com.poste

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.dp
import com.poste.http.RegisterRequest
import com.poste.http.RetrofitClient
import com.poste.reusables.EntryBox
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
    BackHandler{
        if (currentStep == RegistrationStep.Email) {
            sharedViewModel.currentScreenState.value = ScreenState.INTRO
        } else {
            val previousOrdinal = currentStep.ordinal - 1
            if (previousOrdinal >= 0) {
                currentStep = RegistrationStep.entries.toTypedArray()[previousOrdinal]
            }
        }
    }
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
                EntryBox("Email", email, { email = it }) { validateEmail(it) }
            }

            RegistrationStep.FirstName -> {
                Text(
                    text = "Next, we'll need your first name",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.height(16.dp))
                EntryBox(
                    "First Name",
                    firstName,
                    { firstName = it }
                ) { validateName(it) }
            }

            RegistrationStep.LastName -> {
                Text(
                    text = "Hi, $firstName. What's your last name?",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.height(16.dp))
                EntryBox("Last Name", lastName, { lastName = it }) { validateName(it) }
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
                ) { validatePassword(it) }
                EntryBox(
                    "Confirm Password",
                    confirmPassword,
                    { confirmPassword = it },
                    isPassword = true
                ) { validateConfirmPassword(password, it) }

            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                when (currentStep) {
                    RegistrationStep.Password -> {
                        if (validateConfirmPassword(password, confirmPassword) == null) {
                            handleRegistration(
                                email = email,
                                firstName = firstName,
                                lastName = lastName,
                                password = password,
                                context = context,
                                sharedViewModel = sharedViewModel
                            )
                        } else {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT)
                                .show()
                            currentStep = RegistrationStep.Password
                        }

                    }

                    else -> {
                        val nextOrdinal = currentStep.ordinal + 1
                        currentStep = RegistrationStep.entries.toTypedArray()[nextOrdinal]
                    }
                }
            },
            enabled = when (currentStep) {
                RegistrationStep.Email -> validateEmail(email) == null
                RegistrationStep.FirstName -> validateName(firstName) == null
                RegistrationStep.LastName -> validateName(lastName) == null
                RegistrationStep.Password -> validatePassword(password) == null && validateConfirmPassword(
                    password,
                    confirmPassword
                ) == null
            },
            modifier = Modifier
                .fillMaxWidth(.6f)
        ) {
            Text(text = if (currentStep == RegistrationStep.Password) "Complete Registration" else "Next")
        }
    }
}

fun handleRegistration(
    email: String,
    firstName: String,
    lastName: String,
    password: String,
    context: Context,
    sharedViewModel: SharedViewModel
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
                    sharedViewModel.currentScreenState.value = ScreenState.INTRO
                } else {
                    val errorMessage = response.errorBody()?.string()?.let { responseBodyString ->
                        try {
                            val jsonObject = JSONObject(responseBodyString)
                            val errors = jsonObject.optJSONArray("email") ?: return@let "Unknown error"
                            if (errors.length() > 0) {
                                errors.optString(0).replaceFirstChar { if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()) else it.toString() }
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