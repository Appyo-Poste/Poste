package com.poste.reusables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    validator: (String) -> String? = { null },
) {
    var isValid by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }

    var hidePassword by remember { mutableStateOf(true) }

    OutlinedTextField(
        maxLines = 1,
        value = state,
        onValueChange = {
            val newText = it.filterNot { char -> char.isWhitespace() }
            onTextChange(newText)
            error = validator(newText) ?: ""
            isValid = error.isEmpty()
        },
        label = { Text(text = label) },
        modifier = Modifier.fillMaxWidth(.85f),
        shape = RoundedCornerShape(16.dp),
        isError = !isValid,
        visualTransformation = if (isPassword && hidePassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = {
            when {
                isPassword -> {
                    val image = if (hidePassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (hidePassword) "Show password" else "Hide password"
                    IconButton(onClick = { hidePassword = !hidePassword }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
                !isValid && state.isNotEmpty() -> {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Filled.Warning, contentDescription = "Error")
                    }
                }
                isValid && state.isNotEmpty() -> {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "Valid")
                    }
                }
            }
        }
    )
    if (!isValid && state.isNotEmpty()) {
        Text(
            text = error,
            color = Color.Red,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}