package com.poste


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.poste.reusables.FolderList
import com.poste.ui.theme.PosteTheme
import com.poste.ui.theme.*

val iconWeight = 0.15f
val totalWeight = 1f - iconWeight
val columnWeight = totalWeight / 3

@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(navController = rememberNavController())
//    DashboardContent()
}


@Composable
fun DashboardScreen(navController: NavHostController) {
    PosteTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column( // master column
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFF7F7F7))
            ) {
                DashboardHeader()

                DashboardContent()

            }


        }
    }

}

@Composable
fun DashboardContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .background(Color(0xFFF7F7F7))
            .background(Color.Red)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(Modifier.weight(iconWeight))

            Text(
                "Name",
                modifier = Modifier
                    .weight(columnWeight)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                color = dashboardColumnTitles,
                fontSize = 14.sp
            )

            Text(
                "Date Modified",
                modifier = Modifier
                    .weight(columnWeight)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                color = dashboardColumnTitles,
                fontSize = 14.sp
            )

            Text(
                "Action",
                modifier = Modifier
                    .weight(columnWeight)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                color = dashboardColumnTitles,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ){
            FolderList(folders = SampleData.FolderListSample)
        }

    }
}


@Composable
fun DashboardHeader() {
    Column( // welcome back, search bar, and icons
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
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
        Spacer(modifier = Modifier.height(8.dp))
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
                    Icon(imageVector = Icons.Outlined.Clear, contentDescription = "Search")
                }
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .align(Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = { /* Do something */ },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryDark, contentColor = Color.Black)
                ) {
                // Place the icon and text inside a Row to have them side by side
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add, // Replace YourIcon with the actual icon you want to use
                        contentDescription = "Icon Description"
                    )
                    Text("New Folder",
                        fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { /* Do something */ },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryDark, contentColor = Color.Black)
            ) {
                // Place the icon and text inside a Row to have them side by side
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add, // Replace YourIcon with the actual icon you want to use
                        contentDescription = "Icon Description"
                    )
                    Text("New Post",
                        fontSize = 12.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}