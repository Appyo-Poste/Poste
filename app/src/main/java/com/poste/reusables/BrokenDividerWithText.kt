package com.poste.reusables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BrokenDividerWithText(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .weight(1f)
                .padding(start = 0.dp, end = 4.dp),
            thickness = 1.dp
        )
        Text(
            text = text,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp, end = 0.dp),
            thickness = 1.dp
        )
    }
}