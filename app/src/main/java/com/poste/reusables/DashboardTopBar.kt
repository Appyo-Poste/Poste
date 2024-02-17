package com.poste.reusables

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.poste.theme.PosteTheme

@Composable
fun Banner(name: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.height(70.dp), contentAlignment = Alignment.BottomStart){
                    Text(
                        text = "Welcome Back $name!",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Box(Modifier.height(70.dp), contentAlignment = Alignment.TopEnd) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "Poste Icon",
                        modifier = Modifier
                            .size(55.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(

            ){
                Button(
                    onClick = { /* Handle button click here */ },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Image(
                        painter = painterResource(id =R.drawable.addcircleicon),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier
                            .size(20.dp)

                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text("New Folder")
                }

                /**
                 * TODO: ADD SEARCHBAR
                 */
                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { /* Handle button click here */ },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Image(
                        painter = painterResource(id =R.drawable.downloadicon),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier
                            .size(20.dp)

                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text("Import File")
                }
            }
        }
    }
}

@Preview
    (showBackground = true)
@Composable
fun BannerPreview() {
    PosteTheme {
        Banner(name = "Alivia")
    }
}