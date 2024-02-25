package com.poste


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


@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(navController = rememberNavController())
}


@Composable
fun DashboardScreen(navController: NavHostController) {
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
                /*
                McConnell Implementation of top bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth(.8f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Welcome Back!",
                        modifier = Modifier
                            .align(Alignment.Bottom),
                        fontSize = 24.sp
                        )
                    Spacer(Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Poste Logo",
                        modifier = Modifier
                            .height(50.dp)
                            .align(Alignment.CenterVertically)
                    )

                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = "Search here...", onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth(.8f)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = {
                        IconButton(onClick = {  }) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {  }) {
                            Icon(imageVector = Icons.Outlined.FilterAlt, contentDescription = "Search")
                        }
                    },

                )

                */
                DashboardTopBar()
                FolderList(folders = SampleData.FolderListSample)
            }
        }
    }

}