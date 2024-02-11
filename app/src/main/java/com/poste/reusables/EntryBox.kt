package com.poste.reusables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun EntryBox(
    label: String,
    state: String,
    onTextChange: (String) -> Unit,
    isPassword: Boolean = false,
    validator: (String) -> String? = { null }, // Validation function
) {
    var isValid by remember { mutableStateOf(true) }

    var error by remember { mutableStateOf("") }
    OutlinedTextField(
        maxLines = 1,
        value = state,
        onValueChange = {
            val newText = it.filterNot { char -> char.isWhitespace() }
            onTextChange(newText)
            error = validator(newText) ?: ""
            isValid = validator(newText) == null // Perform validation on each text change
        },
        label = { Text(text = label) },
        modifier = Modifier.fillMaxWidth(.8f),
        shape = RoundedCornerShape(16.dp),
        isError = !isValid, // Show error state based on validation
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = {
            if (!isValid && state.isNotEmpty()) {
                Icon(imageVector = Icons.Filled.Warning, contentDescription = "Invalid Input")
            }
        }
    )

    // Optionally display an error message
    if (!isValid && state.isNotEmpty()) {
        Text(
            text = error,
            color = Color.Red,
            // style caption
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}