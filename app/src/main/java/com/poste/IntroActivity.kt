package com.poste

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
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
            composable("dynamic") { DynamicScreen(sharedViewModel) }
        }
    }
}

@Composable
fun DynamicScreen(sharedViewModel: SharedViewModel) {
    val currentState = sharedViewModel.currentScreenState.value
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
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
        }
    }

}

@Preview
@Composable
fun test() {
    PosteTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Test")
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
            //containerColor = LogoLight,
            //contentColor = LogoDark,
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
                //TODO: Handle login navigation
            },
            modifier = Modifier
                .fillMaxWidth(.6f)
        ) {
            Text(text = "Login")
        }
    }

}

enum class RegistrationStep { Email, FirstName, LastName, Password }

