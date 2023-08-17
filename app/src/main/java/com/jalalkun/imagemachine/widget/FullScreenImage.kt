package com.jalalkun.imagemachine.widget

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Popup
import coil.compose.AsyncImage

@Composable
fun FullScreenImage(
    image: String,
    onDismissRequest: () -> Unit
){
    BackHandler {
        onDismissRequest()
    }
    Popup(
        onDismissRequest = onDismissRequest
    ) {
        AsyncImage(model = image, contentDescription = "", modifier = Modifier.fillMaxSize().clickable { onDismissRequest() }, contentScale = ContentScale.Crop)
    }
}