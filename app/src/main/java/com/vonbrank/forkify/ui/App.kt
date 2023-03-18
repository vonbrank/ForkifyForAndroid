package com.vonbrank.forkify.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vonbrank.forkify.ui.component.SearchAppBar
import com.vonbrank.forkify.ui.page.RecipeSearch
import com.vonbrank.forkify.ui.theme.ForkifyTheme
import com.vonbrank.forkify.ui.viewmodel.RecipeSearchViewModal

@Composable
fun App() {
    ForkifyTheme {

        val recipeSearchViewModal: RecipeSearchViewModal = viewModel()

        Scaffold(topBar = {
            SearchAppBar(title = {
                Text(text = "Forkify")
            }, actions = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add recipe icon",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = "Add recipe icon",
                        tint = Color.White
                    )
                }
            },
                handleSearch = { query -> recipeSearchViewModal.searchRecipe(query) })
        }) { paddingValues ->
            RecipeSearch(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            )
        }
    }
}