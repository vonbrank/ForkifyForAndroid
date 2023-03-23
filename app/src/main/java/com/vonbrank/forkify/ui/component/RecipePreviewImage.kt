package com.vonbrank.forkify.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun RecipePreviewImage(imageUrl: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
    ) {

        var imageLoaded by remember { mutableStateOf(false) }
        val foregroundColorAlpha by remember {
            derivedStateOf {
                if (imageLoaded) 0.5F else 0F
            }
        }

        AsyncImage(
            model = imageUrl,
            contentDescription = "Recipe preview image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            onSuccess = {
                imageLoaded = true
            }
        )
        if (!imageLoaded) {
            Image(
                imageVector = Icons.Default.RestaurantMenu,
                contentDescription = "Placeholder icon",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary.copy(alpha = 0.5F))
        )
    }
}