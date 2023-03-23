package com.vonbrank.forkify.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vonbrank.forkify.logic.modal.RecipePreview
import com.vonbrank.forkify.ui.component.RecipePreviewItem
import com.vonbrank.forkify.ui.viewmodel.BookmarkViewModal

@Composable
fun Bookmark(
    modifier: Modifier = Modifier,
    bookmarkViewModal: BookmarkViewModal = viewModel(),
    onRecipePreviewClick: (recipePreview: RecipePreview) -> Unit = {}
) {

    val bookmarkList by bookmarkViewModal.recipeBookmarkList.observeAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item() {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Bookmarks".uppercase(),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colors.primary,
                )
            }
        }

        items(bookmarkList ?: ArrayList()) { recipePreview ->
            RecipePreviewItem(
                title = recipePreview.title,
                publisher = recipePreview.publisher,
                onClicked = {
                    onRecipePreviewClick(recipePreview)
                },
                imageUrl = recipePreview.imageUrl
            )
        }
    }
}