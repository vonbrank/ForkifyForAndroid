package com.vonbrank.forkify.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecipePreviewItem(
    title: String,
    publisher: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit = {},
) {

    Row(modifier = modifier
        .fillMaxWidth()
        .clickable {
            onClicked()
        }
        .padding(vertical = 12.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        RecipePreviewImage(
            imageUrl = imageUrl,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column() {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle2.copy(
                    color = MaterialTheme.colors.primary,
                    fontSize = 18.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = publisher, style = MaterialTheme.typography.body2.copy(
                    fontSize = 14.sp
                )
            )
        }
    }
}