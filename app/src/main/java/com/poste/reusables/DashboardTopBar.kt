package com.poste.reusables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.poste.ui.theme.PosteTheme

@Composable
fun DashboardTopBar() {

    var query by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false)}

    CreateFolderDialog(showDialog = showDialog, onDismiss = { showDialog = false })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
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
                        text = "Welcome Back!",
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

        SearchBar(
            query = query,
            onQueryChanged = { newQuery ->
                query = newQuery
            },
            onSearch = {
                // Perform search action here
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(

            ){
                Button(
                    onClick = { showDialog = true },
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

                    Text("New Post")
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.Transparent),
        shape = RoundedCornerShape(10.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search",
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp)
            )
        },
        placeholder = {
            Text(text = "Search here...")
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = {
                        // Clear the search query
                        onQueryChanged("")
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.close_small),
                        contentDescription = "Clear search",
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        /**
                         * TODO: Include advanced search options
                         */
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.filter),
                        contentDescription = "Advanced search",
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}


@Preview
    (showBackground = false)
@Composable
fun SearchBarEmptyPreview(){
    PosteTheme {
        SearchBar(
            query = "",
            onQueryChanged = {},
            onSearch = {}
        )
    }
}

@Preview
    (showBackground = false)
@Composable
fun SearchBarQueryPreview(){
    PosteTheme {
        SearchBar(
            query = "Paris",
            onQueryChanged = {},
            onSearch = {}
        )
    }
}

@Preview
    (showBackground = true)
@Composable
fun BannerPreview() {
    PosteTheme {
        DashboardTopBar()
    }
}