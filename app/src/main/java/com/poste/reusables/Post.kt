package com.poste.reusables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.poste.models.Post
import com.poste.ui.theme.PosteTheme
import java.time.format.DateTimeFormatter

@Composable
private fun PostComposable(post: Post){
    val uriHandler = LocalUriHandler.current
    Box(
        modifier = Modifier.clickable {
            /**
             * TODO: Implement what happens when it's clicked
             */
            uriHandler.openUri(post.url)
        }
    ){

        Column(
            modifier = Modifier
                .width(140.dp)
                .padding(all = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = post.title, maxLines = 1, overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(6.dp))

            Image(
                painter = painterResource(R.drawable.document_draft),
                contentDescription = "Icon for folder",
                modifier = Modifier
                    .size(75.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))


            /**

            Text(
            text = post.url, // Convert URL to string
            style = TextStyle(
            fontSize = 16.sp,
            color = Color.Blue,
            textDecoration = TextDecoration.Underline
            )
            )

             */

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = post.date.format(DateTimeFormatter.ofPattern("dd/MM/yy")), // Format date
                style = TextStyle(fontSize = 14.sp, color = Color.DarkGray)
            )
        }
    }
}

@Preview
    (showBackground = true)
@Composable
fun PostPreview(){
    PosteTheme {
        PostComposable(post = SampleData.PostSample)
    }
}



