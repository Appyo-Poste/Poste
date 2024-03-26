package com.poste.reusables

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poste.models.Folder
import com.poste.ui.theme.PosteTheme

@Composable
private fun FolderComposable(folder: Folder) {
    var showEditFolderDialog by remember { mutableStateOf(false) }
    var showShareFolderDialog by remember { mutableStateOf(false) }

    EditFolderDialog(showDialog = showEditFolderDialog, folder = folder, onDismiss = { showEditFolderDialog = false })

    ShareFolderDialog(showDialog = showShareFolderDialog, folder = folder, onDismiss = { showShareFolderDialog = false })

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
            Icon(Icons.Default.Folder, contentDescription = "Icon for folder",
                modifier = Modifier
                    .size(55.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(Modifier.size(192.dp), contentAlignment = Alignment.Center) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(90.dp),contentAlignment = Alignment.Center) {
                    Column {
                        Text(text = folder.title, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        Text(text = "${folder.file_count} file" + if (folder.file_count != 1) "s" else "")
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = folder.getShortCreationDate()
                    )
                }
            }
        }
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row {
                Icon(Icons.Outlined.Edit, contentDescription = "Icon for edit",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .clickable { showEditFolderDialog = true }
                )
                Icon(Icons.Outlined.Upload, contentDescription = "Icon for move folder",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .clickable {
                                   /* TODO: add click functionality for move folder */
                                   }
                )
                Icon(Icons.Outlined.Share, contentDescription = "Icon for share",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .clickable {
                            showShareFolderDialog = true
                        }
                )
            }
        }
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
fun FolderListPreview(){
    PosteTheme {
        FolderList(folders = SampleData.FolderListSample)
    }
}