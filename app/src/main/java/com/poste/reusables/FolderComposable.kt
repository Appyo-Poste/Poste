package com.poste.reusables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
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
            .padding(start = 7.5.dp, end = 7.5.dp)
            .clickable {
                /* TODO: add click functionality */
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(0.5f), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.folder),
                contentDescription = "Poste Icon",
                modifier = Modifier
                    .size(45.dp)

            )
            if (folder.file_count != 0) {
                Text(text = "${folder.file_count} file${if (folder.file_count != 1) "s" else ""}",
                    style = MaterialTheme.typography.labelSmall
                    )
            }
            if (folder.folder_count != 0) {
                Text(text = "${folder.folder_count} folder${if (folder.folder_count != 1) "s" else ""}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            if (folder.file_count == 0 && folder.folder_count == 0){
                Text(
                    text = "Empty \nfolder!",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = folder.title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )


        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = folder.getShortCreationDate(),
            Modifier.padding(end = 6.dp),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            )

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