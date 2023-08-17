package com.jalalkun.imagemachine.widget.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ItemImages(
    path: String,
    delete: (() -> Unit)?
){
    OutlinedCard(
        modifier = Modifier.padding(8.dp)
            .then(if (delete != null) Modifier.clickable { delete() } else Modifier)
    ) {

    }
}