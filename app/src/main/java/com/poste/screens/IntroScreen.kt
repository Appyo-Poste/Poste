package com.poste.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.poste.reusables.RegisterScreen
import com.poste.reusables.handleLogin
import com.poste.reusables.BrokenDividerWithText
import com.poste.reusables.EntryBox
import com.poste.reusables.rememberImeState
import com.poste.reusables.validateEmail
import com.poste.reusables.validatePassword
import com.poste.ui.theme.PosteTheme

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
    PosteTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "intro") {
            composable("intro") { IntroScreen(navController) }
            composable("register") { RegisterScreen(navController) }
            composable("dashboard") { DashboardScreen(navController) }
        }
    }
}



@Composable
fun IntroScreen(navController: NavController) {
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
        }
    }
    Surface(color = MaterialTheme.colorScheme.background) {
        IntroContent(navController)
    }
}

@Composable
fun IntroContent(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    PosteTheme {
        val imeState = rememberImeState()
        val scrollState = rememberScrollState()

        LaunchedEffect(key1 = imeState.value) {
            if (imeState.value) {
                scrollState.animateScrollTo(scrollState.maxValue, tween(300))
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
            Row(
                modifier = Modifier.fillMaxWidth(.85f),
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logowithtext),
                    contentDescription = "Poste Logo",
                    modifier = Modifier
                        .height(100.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(.85f),
                horizontalArrangement = Arrangement.Start
            ) {
                Text("Find, save, retrieve. Simple as that.", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(.85f),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Log In",
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(.85f),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Email",
                    fontSize = 16.sp
                )
            }
            EntryBox("Email", email, { email = it }) { validateEmail(it) }

            // space between entry box and 'password' text'
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(.85f),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Password",
                    fontSize = 16.sp
                )
            }
            EntryBox("Password", password, { password = it }, isPassword = true) { validatePassword(password) }

            // space between password box and 'forgot password' link
            Spacer(modifier = Modifier.height(48.dp))
            Row(
                modifier = Modifier.fillMaxWidth(.75f),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    "Forgot Password?",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clickable { /* Handle click here */ }
                )
            }
            Spacer(modifier = Modifier
                .height(16.dp))
            Button(
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    handleLogin(email, password, context, navController, coroutineScope)
                },
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth(.85f)
                    .height(48.dp)

            ){
                Text(text = "Get Started")
            }

            Spacer(modifier = Modifier.height(32.dp))
            BrokenDividerWithText(text = "or")
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(.85f),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Don't have an account?",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Sign Up",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("register")
                        }
                )

            }
        }
    }
}
