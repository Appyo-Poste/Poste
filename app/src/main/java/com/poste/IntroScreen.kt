package com.poste

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.poste.reusables.rememberImeState
import com.poste.theme.LogoLight
import com.poste.theme.PosteTheme


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
        val sharedViewModel = remember { SharedViewModel() }
        NavHost(navController = navController, startDestination = "dynamic") {
            composable("dynamic") { DynamicScreen(sharedViewModel, navController) }
            composable("dashboard") { DashboardScreen(navController) }
        }
    }
}

@Composable
fun DynamicScreen(sharedViewModel: SharedViewModel, navController: NavController) {
    val currentState = sharedViewModel.currentScreenState.value
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
        }
    }
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Intro Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
            )
            AnimatedVisibility(visible = currentState == ScreenState.INTRO) {
                IntroContent(sharedViewModel = sharedViewModel)
            }
            AnimatedVisibility(visible = currentState == ScreenState.REGISTER) {
                RegisterContent(sharedViewModel = sharedViewModel)
            }
            AnimatedVisibility(visible = currentState == ScreenState.LOGIN) {
                LoginContent(sharedViewModel = sharedViewModel, navController = navController)
            }
        }
    }

}
@Composable
fun IntroContent(sharedViewModel: SharedViewModel) {

    Log.d("ThemeDebug", "Current primary color: ${MaterialTheme.colorScheme.primary}")

    Column {
        Spacer(modifier = Modifier.height(16.dp))
        FloatingActionButton(
            onClick = {
                sharedViewModel.currentScreenState.value = ScreenState.REGISTER
            },
            modifier = Modifier
                .fillMaxWidth(.6f),
        ) {
            Text(text = "Register")
        }
        Spacer(modifier = Modifier.height(16.dp))
        FloatingActionButton(
            containerColor = LogoLight,
            onClick = {
                sharedViewModel.currentScreenState.value = ScreenState.LOGIN
            },
            modifier = Modifier
                .fillMaxWidth(.6f)
        ) {
            Text(text = "Login")
        }
    }

}

enum class RegistrationStep { Email, FirstName, LastName, Password }

