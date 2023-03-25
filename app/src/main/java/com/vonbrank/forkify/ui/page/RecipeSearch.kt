package com.vonbrank.forkify.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vonbrank.forkify.logic.modal.RecipePreview
import com.vonbrank.forkify.ui.component.ModalBottomSheet
import com.vonbrank.forkify.ui.component.RecipePreviewItem
import com.vonbrank.forkify.ui.component.SearchAppBar
import com.vonbrank.forkify.ui.route.RecipeDetail
import com.vonbrank.forkify.ui.viewmodel.RecipeSearchViewModal
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecipeSearch(navController: NavController) {
    val recipeSearchViewModal: RecipeSearchViewModal = viewModel()

    val addRecipeModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val bookmarkModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var currentBottomSheetType by rememberSaveable {
        mutableStateOf(BottomSheetType.NONE)
    }

    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SearchAppBar(title = {
                Text(text = "Forkify")
            }, actions = {
                IconButton(onClick = {
                    currentBottomSheetType = BottomSheetType.ADD_RECIPE
                    scope.launch {
                        addRecipeModalBottomSheetState.show()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add recipe icon",
                        tint = Color.White
                    )
                }
                IconButton(onClick = {
                    currentBottomSheetType = BottomSheetType.BOOKMARK
                    scope.launch {
                        bookmarkModalBottomSheetState.show()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = "Bookmark icon",
                        tint = Color.White
                    )
                }
            },
                handleSearch = { query -> recipeSearchViewModal.searchRecipe(query) })

            RecipeSearchResult(
                modifier = Modifier.weight(1f),
                onRecipePreviewClick = { recipePreview ->
                    RecipeDetail.sendArguments(navController, recipePreview)
                    navController.navigate(RecipeDetail.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }

    ModalBottomSheet(
        sheetState = addRecipeModalBottomSheetState,
        sheetContent = {
            AddRecipe()
        },
    )
    ModalBottomSheet(
        sheetState = bookmarkModalBottomSheetState,
        sheetContent = {
            Bookmark(onRecipePreviewClick = { recipePreview ->
                RecipeDetail.sendArguments(navController, recipePreview)
                navController.navigate(RecipeDetail.route) {
                    launchSingleTop = true
                }
            })
        }
    )
}


@Composable
fun RecipeSearchResult(
    modifier: Modifier = Modifier,
    recipeSearchViewModal: RecipeSearchViewModal = viewModel(),
    onRecipePreviewClick: (recipePreview: RecipePreview) -> Unit = {}
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
}

enum class BottomSheetType {
    ADD_RECIPE,
    BOOKMARK,
    NONE,
}