package com.poste.reusables

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun <T> GenericDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<T>, // Generic type T
    onItemSelected: (T) -> Unit // Generic type T for selected item
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = { Text(item.toString(), style = MaterialTheme.typography.labelMedium) },
                onClick = {
                    onItemSelected(item)
                    onDismissRequest()
                }
            )
        }
    }
}