package com.vonbrank.forkify.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecipePreviewItem(modifier: Modifier = Modifier) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { }) {
        Image(
            imageVector = Icons.Default.Info,
            contentDescription = "Recipe preview image",
            modifier = Modifier.size(48.dp)
        )
        Column() {
            Text(text = "Recipe Title")
            Text(text = "Recipe Description")
        }
    }
}