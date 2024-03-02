package com.poste.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.poste.reusables.DashboardTopBar
import com.poste.reusables.FolderList
import com.poste.ui.theme.PosteTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.poste.viewmodels.MainViewModel
import kotlinx.coroutines.launch


@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(navController = rememberNavController())
}


@Composable
fun DashboardScreen(navController: NavHostController) {
    val dashboardViewModel: MainViewModel = viewModel()
    val data by dashboardViewModel.data.observeAsState()
    val errorMessage by dashboardViewModel.errorMessage.observeAsState()


    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            dashboardViewModel.fetchData(context)
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
                data?.folders?.let { folders ->
                    FolderList(folders = folders)
                }
            }
        }
    }
}