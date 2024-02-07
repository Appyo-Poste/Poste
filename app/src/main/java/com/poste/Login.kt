package com.poste

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poste.reusables.EntryBox
import com.poste.reusables.validateEmail
import com.poste.reusables.validatePassword


@Preview
@Composable
fun TestLoginContent(){
    val sharedViewModel = SharedViewModel()
    LoginContent(sharedViewModel)
}

@Composable
fun LoginContent(sharedViewModel: SharedViewModel) {
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
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                sharedViewModel.currentScreenState.value = ScreenState.INTRO
            },
            enabled = validateEmail(email) == null && validatePassword(password) == null,
            modifier = Modifier
                .fillMaxWidth(.6f)
        ) {
            Text(text = "Login")
        }
    }
}
