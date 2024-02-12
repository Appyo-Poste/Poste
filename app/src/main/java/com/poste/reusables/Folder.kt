package com.poste.reusables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.poste.SampleData
import com.poste.theme.PosteTheme
import java.time.LocalDate

data class Folder(val name: String, val numFiles: Int, val date: LocalDate)

@Composable
private fun FolderComposable(folder: Folder) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .height(80.dp)
            .padding(start = 7.5.dp, end = 7.5.dp)
            .clickable {
                /* TODO: add click functionality */
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(55.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.folder_icon),
                contentDescription = "Icon for folder"
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(Modifier.size(192.dp), contentAlignment = Alignment.Center) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(90.dp), contentAlignment = Alignment.Center) {
                    Column {
                        Text(text = folder.name, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        Text(text = "${folder.numFiles} files")
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = folder.date.toString()
                )
            }
        }
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row {
                Image(
                    painter = painterResource(R.drawable.post_icon),
                    contentDescription = "Icon for edit",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            /* TODO: add click functionality for edit */
                        },
                )
                Image(
                    painter = painterResource(R.drawable.post_icon),
                    contentDescription = "Icon for upload",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            /* TODO: add click functionality for upload */
                        },
                )
                Image(
                    painter = painterResource(R.drawable.post_icon),
                    contentDescription = "Icon for share",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            /* TODO: add click functionality for share */
                        },
                )
            }
        }
    }
}

@Composable
fun FolderList(folders: List<Folder>){
    LazyColumn {
        items(folders) { folder ->
            FolderComposable(
                folder
            )
        }
    }
}


@Preview(
    showBackground = true
)
@Composable
fun PreviewFolders(){
    PosteTheme {
        FolderList(folders = SampleData.FolderListSample)
    }
}

@Preview(
    showBackground = true
)
@Composable
fun FolderPreview() {
    PosteTheme {
        FolderComposable(folder = SampleData.FolderSample)
    }
}