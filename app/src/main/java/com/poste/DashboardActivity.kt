package com.poste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.poste.theme.PosteTheme
import java.time.LocalDate

private enum class ObjectType{Folder, SharedFolder,Post}

data class Folder(val name: String, val numFiles: Int, val date: LocalDate)

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
        .height(80.dp)
        .padding(start=7.5.dp, end = 7.5.dp)
        .clickable {
            /* TODO: add click functionality */
        },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(85.dp)
        ){
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
        }
        Box(Modifier.size(182.dp), contentAlignment = Alignment.Center){
            Row(verticalAlignment = Alignment.CenterVertically){
                Box(Modifier.size(85.dp), contentAlignment = Alignment.Center){
                    Column {
                        Text(text = name, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        Text(text = "$numFiles files")
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = date.toString()
                )
            }
        }
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
            Row{
                Image(
                    painter = painterResource(R.drawable.post_icon),
                    contentDescription = "Icon for edit",
                    modifier = Modifier
                        .size(30.dp)
                )
                Image(
                    painter = painterResource(R.drawable.post_icon),
                    contentDescription = "Icon for upload",
                    modifier = Modifier
                        .size(30.dp)
                )
                Image(
                    painter = painterResource(R.drawable.post_icon),
                    contentDescription = "Icon for share",
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }
    }
}


@Composable
fun FolderList(folders: List<Folder>){
    LazyColumn{
        items(folders){folder ->
            DashboardObject(name = folder.name , numFiles = folder.numFiles , date = folder.date,
                objectType = ObjectType.Folder)
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun PreviewFolders(){
    PosteTheme {
        FolderList(folders = SampleData.FolderSample)
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
