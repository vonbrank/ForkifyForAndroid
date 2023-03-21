package com.vonbrank.forkify.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vonbrank.forkify.ui.component.SearchAppBar
import com.vonbrank.forkify.ui.page.AddRecipe
import com.vonbrank.forkify.ui.page.RecipeSearch
import com.vonbrank.forkify.ui.theme.ForkifyTheme
import com.vonbrank.forkify.ui.viewmodel.RecipeSearchViewModal
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun App() {
    ForkifyTheme {
        val recipeSearchViewModal: RecipeSearchViewModal = viewModel()
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
        val scope = rememberCoroutineScope()

        BottomSheetScaffold(
            sheetPeekHeight = 0.dp,
            scaffoldState = bottomSheetScaffoldState,
            sheetShape = RoundedCornerShape(16.dp),
            sheetContent = {
                AddRecipe()
            },
            topBar = {

                SearchAppBar(title = {
                    Text(text = "Forkify")
                }, actions = {
                    IconButton(onClick = {
                        scope.launch {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    }) {
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
            },
        ) { paddingValues ->
            RecipeSearch(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            )
        }
    }
}