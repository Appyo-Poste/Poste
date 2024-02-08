package com.poste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import java.time.LocalDate

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
                    DashboardObject("Test Folder", 12, LocalDate.now(), ObjectType.Folder)
                }
            }
        }
    }
}

@Composable
private fun DashboardObject(name: String, numFiles: Int, date: LocalDate, objectType: ObjectType ) {
    Row (modifier = Modifier
        .padding(all = 8.dp)
        .fillMaxWidth()
        .clickable {
            /* TODO: add click functionality */
        },
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically) {
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
            /*
            TODO: POSTS NEED TO BE REDONE ACCORDING TO NEW PROTOTYPES
            ObjectType.Post ->
                Image(
                    painter = painterResource(R.drawable.post_icon),
                    contentDescription = "Icon for post",
                    modifier = Modifier
                        .size(80.dp)
                )*/
            else -> {}
        }
        Column {
            Text(text = name)
            Text(text = "$numFiles files")
        }
        Text(
            text = date.toString()
        )
        Row{
            Image(
                painter = painterResource(R.drawable.post_icon),
                contentDescription = "Icon for edit",
                modifier = Modifier
                    .size(35.dp)
            )
            Image(
                painter = painterResource(R.drawable.post_icon),
                contentDescription = "Icon for upload",
                modifier = Modifier
                    .size(35.dp)
            )
            Image(
                painter = painterResource(R.drawable.post_icon),
                contentDescription = "Icon for share",
                modifier = Modifier
                    .size(35.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FolderPreview() {
    PosteTheme {
        DashboardObject("Test Folder", 10, LocalDate.now(), ObjectType.Folder)
    }
}

@Preview(showBackground = true)
@Composable
fun SharedFolderPreview() {
    PosteTheme {
        DashboardObject("Shared Folder", 8, LocalDate.now(), ObjectType.SharedFolder)
    }
}

/*
TODO: POSTS NEED TO BE REDONE ACCORDING TO NEW PROTOTYPES
@Preview(showBackground = true)
@Composable
fun PostPreview() {
    PosteTheme {
        DashboardObject("Test Post", LocalDate.now(), ObjectType.Post)
    }
}*/
