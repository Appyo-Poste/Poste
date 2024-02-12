package com.poste

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.poste.reusables.Folder
import java.time.LocalDate

@Composable
fun DashboardScreen(navController: NavHostController) {
    Text(text="Dashboard")
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Folder("Test", 13, LocalDate.now())
    }
}