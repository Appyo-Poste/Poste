package com.poste.reusables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.myapplication.R
import com.poste.models.Folder
import com.poste.ui.theme.PosteTheme

@Preview
@Composable
fun ShareFolderPreview() {
    PosteTheme {
        ShareFolderDialog(
            showDialog = true,
            folder = SampleData.FolderSample,
            {})
    }
}

@Composable
fun ShareFolderDialog(
    showDialog: Boolean,
    folder: Folder,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(25.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Share Folder",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold)
                    )

                    Image(
                        painter = painterResource(R.drawable.folder),
                        contentDescription = "Poste Icon",
                        modifier = Modifier
                            .size(120.dp)

                    )

                    Text(
                        text = folder.title,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(16.dp))


/*
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    OutlinedTextField(
                        value = folderName,
                        onValueChange = {
                            folderName = it

                        },
                        singleLine = true,
                        label = { Text("Name") },
                        modifier = Modifier
                            .fillMaxWidth(.9f),
                        shape = RoundedCornerShape(24.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    OutlinedTextField(
                        value = folderDescription,
                        onValueChange = {
                            if (it.length <= folderDescriptionMaxCharacters) {
                                folderDescription = it
                            }
                        },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .heightIn(140.dp),
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 4,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(.9f),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Text(
                            text = "Max 250 characters",
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }*/

                    Spacer(modifier = Modifier.height(12.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        SaveCancelTabs(
                            onSave = {
                                /*TODO*/
                            },
                            onCancel = onDismiss
                        )
                    }
                }
            }
        }
    }
}

@Preview
    (showBackground = true)
@Composable
fun PreviewShareItemComposable(){
    PosteTheme {
        Column {
            ShareItemComposable(
                emailSharedWith = "jason@gmail.com",
                sharelevel = "Can view"
            )
            ShareItemComposable(
                emailSharedWith = "davidcookoatmeal@email.com",
                sharelevel = "Can view"
            )
        }
    }
}

@Composable
fun ShareItemComposable(
    emailSharedWith: String,
    sharelevel: String){
    Box(
        modifier = Modifier
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                text = emailSharedWith,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 5.dp),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = sharelevel,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(R.drawable.close),
                contentDescription = "Icon for delete share",
                modifier = Modifier
                    .size(18.dp)
                    .clickable {
                        /*TODO*/
                    }
                    .padding(end = 5.dp),
            )
        }
    }

}