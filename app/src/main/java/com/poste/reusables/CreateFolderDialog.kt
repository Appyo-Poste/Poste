package com.poste.reusables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.poste.ui.theme.PosteTheme
import com.poste.ui.theme.newLogoDark

@Preview
@Composable
fun CreateFolderPreview() {
    PosteTheme {
        CreateFolderDialog(true, {})
    }
}

@Composable
fun CreateFolderDialog(showDialog: Boolean, onDismiss: () -> Unit) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(25.dp),
            ) {
                Column{

                    var folderName by remember { mutableStateOf("") }
                    var folderDescription by remember { mutableStateOf("") }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text="Create Folder",
                        modifier=Modifier
                            .align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    OutlinedTextField(
                        value = folderName,
                        onValueChange = { folderName = it },
                        label = { Text("Name") },
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(24.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text="Description",
                        modifier=Modifier.align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    OutlinedTextField(
                        value = folderDescription,
                        onValueChange = { folderDescription = it },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .align(Alignment.CenterHorizontally)
                            .heightIn(140.dp),
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 4,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(.9f),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Text(
                            text="Max 250 characters",
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        SaveCancelTabs(
                            onSave = { /*TODO*/ },
                            onCancel = onDismiss
                        )
                    }
                }
            }
        }
    }
}

/**
 * Represents an action tab for the Create Folder Dialog.
 * The [text] is the text to be displayed on the tab, and [onClick] is the action to be performed
 * when the tab is clicked -- pass the action as a lambda function.
 * The [isBold] parameter is optional and is used to make the text bold.
 */
@Composable
fun ActionTab(text: String, onClick: () -> Unit, isBold: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = if (isBold) MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            else MaterialTheme.typography.labelMedium,
            color = newLogoDark,

        )
    }
}

/**
 * Creates the Save and Cancel tabs. The [onSave] and [onCancel] are the actions to be performed
 * when the respective tabs are clicked -- pass the actions as lambda functions.
 * @param onSave: The action to be performed when the Save tab is clicked.
 * @param onCancel: The action to be performed when the Cancel tab is clicked.
 */
@Composable
fun SaveCancelTabs(onSave: () -> Unit, onCancel: () -> Unit) {
    Column {
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 0.dp, end = 4.dp),
            thickness = 1.dp
        )
        ActionTab(
            text = "Save",
            onClick = onSave,
            isBold = true
        )
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 0.dp, end = 4.dp),
            thickness = 1.dp
        )
        ActionTab(
            text = "Cancel",
            onClick = onCancel,
        )
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 0.dp, end = 4.dp),
            thickness = 1.dp
        )
    }
}
