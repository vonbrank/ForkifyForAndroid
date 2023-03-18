package com.vonbrank.forkify.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vonbrank.forkify.logic.modal.RecipePreview
import com.vonbrank.forkify.ui.component.RecipePreviewItem
import com.vonbrank.forkify.ui.viewmodel.RecipeSearchViewModal

@Composable
fun RecipeSearch(
    modifier: Modifier = Modifier,
    recipeSearchViewModal: RecipeSearchViewModal = viewModel()
) {

    val recipeList by recipeSearchViewModal.recipeListLivaData.observeAsState()
    val loadingRecipe by recipeSearchViewModal.loadingRecipe.observeAsState()

    if (loadingRecipe != null && loadingRecipe!!) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(recipeList ?: ArrayList()) { recipePreview ->
                RecipePreviewItem(title = recipePreview.title, publisher = recipePreview.publisher)
            }
        }
    }
}
