package com.poste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.poste.theme.PosteTheme

private enum class ObjectType{Folder, SharedFolder,Post}

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PosteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DashboardObject("Test Folder",ObjectType.Folder)
                }
            }
        }
    }
}

@Composable
private fun DashboardObject(name: String, objectType: ObjectType ) {
    Column (modifier = Modifier
                    .padding(all = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
        when (objectType){
            ObjectType.Folder ->
                Image(
                    painter = painterResource(R.drawable.folder_icon),
                    contentDescription = "Icon for folder",
                    modifier = Modifier
                        .size(80.dp)
                )
            ObjectType.SharedFolder ->
                Image(
                    painter = painterResource(R.drawable.folder_icon_shared),
                    contentDescription = "Icon for shared folder",
                    modifier = Modifier
                        .size(80.dp)
                )
            ObjectType.Post ->
                Image(
                    painter = painterResource(R.drawable.post_icon),
                    contentDescription = "Icon for folder",
                    modifier = Modifier
                        .size(80.dp)
                )
        }
        Text(
            text = name
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FolderPreview() {
    PosteTheme {
        DashboardObject("Test Folder",ObjectType.Folder)
    }
}

@Preview(showBackground = true)
@Composable
fun SharedFolderPreview() {
    PosteTheme {
        DashboardObject("Shared Folder", ObjectType.SharedFolder)
    }
}

@Preview(showBackground = true)
@Composable
fun PostPreview() {
    PosteTheme {
        DashboardObject("Test Post", ObjectType.Post)
    }
}