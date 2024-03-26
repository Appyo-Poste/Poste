package com.poste.reusables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.myapplication.R
import com.poste.ui.theme.PosteTheme
import kotlinx.coroutines.delay

@Composable
fun DropdownList(itemList: List<String>, selectedIndex: Int, modifier: Modifier, onItemClick: (Int) -> Unit) {
    var showDropdown by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        // button
        Box(
            modifier = modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        showDropdown = !showDropdown
                    },
                    indication = null
                )
                .clip(RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = itemList[selectedIndex],
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
                    modifier = Modifier.padding(all = 3.dp))
                Image(
                    painter = painterResource(R.drawable.expand_more),
                    contentDescription = "Icon for expand more",
                    modifier = Modifier
                        .size(15.dp)
                )
            }
        }

        // dropdown list
        Box() {
            if (showDropdown) {
                var dissmissRequest by rememberSaveable { mutableStateOf(false) }
                Popup(
                    alignment = Alignment.TopCenter,
                    properties = PopupProperties(
                        excludeFromSystemGesture = true,
                    ),
                    // to dismiss on click outside
                    onDismissRequest = {
                        dissmissRequest = true
                    }
                ) {
                    if (dissmissRequest){
                        LaunchedEffect(Unit){
                            delay(100)
                            showDropdown=false
                            dissmissRequest=false
                        }
                    }
                    Column(
                        modifier = modifier
                            .heightIn(max = 90.dp)
                            .verticalScroll(state = scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        itemList.onEachIndexed { index, item ->
                            Divider(thickness = 1.dp, color = Color.LightGray)
                            if (index != selectedIndex){
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onItemClick(index)
                                            showDropdown = !showDropdown
                                        }
                                        .padding(end = 15.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = item,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDropdownList(){
    val itemList = listOf<String>("Can View", "Can Edit", "Full Access")
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    PosteTheme {
        DropdownList(
            itemList = itemList, selectedIndex = selectedIndex, modifier = Modifier.width(100.dp),
            onItemClick = {selectedIndex = it}
        )
    }
}