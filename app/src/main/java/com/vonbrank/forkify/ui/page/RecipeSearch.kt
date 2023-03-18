package com.vonbrank.forkify.ui.page

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vonbrank.forkify.ui.component.RecipePreviewItem

@Composable
fun RecipeSearch(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        for (i in 1..20) {
            item {
                RecipePreviewItem()
            }
        }
    }
}