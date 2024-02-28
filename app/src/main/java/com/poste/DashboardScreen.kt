package com.poste


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.poste.reusables.DashboardTopBar
import com.poste.reusables.FolderList
import com.poste.ui.theme.PosteTheme
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.poste.tokens.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(navController = rememberNavController())
}


@Composable
fun DashboardScreen(navController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var token by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            token = TokenManager.getTokenFlow(context).first()
            Log.d("DashboardScreen", "Token: $token")
        }
    }

    PosteTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                DashboardTopBar()
                FolderList(folders = SampleData.FolderListSample)
            }
        }
    }

}