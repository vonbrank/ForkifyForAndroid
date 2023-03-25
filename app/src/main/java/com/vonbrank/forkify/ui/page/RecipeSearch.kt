package com.vonbrank.forkify.ui.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vonbrank.forkify.logic.modal.RecipePreview
import com.vonbrank.forkify.ui.component.ModalBottomSheet
import com.vonbrank.forkify.ui.component.RecipePreviewItem
import com.vonbrank.forkify.ui.component.SearchAppBar
import com.vonbrank.forkify.ui.route.RecipeDetail
import com.vonbrank.forkify.ui.theme.RecipeSearchResultPagerButtonTheme
import com.vonbrank.forkify.ui.viewmodel.RecipeSearchViewModal
import kotlinx.coroutines.launch
import kotlin.math.ceil

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
    val MAX_PAGE_ITEM_NUMBER = 10;
    val recipeList by recipeSearchViewModal.recipeListLivaData.observeAsState()
    val loadingRecipe by recipeSearchViewModal.loadingRecipe.observeAsState()

    if (loadingRecipe != null && loadingRecipe!!) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {

        var currentPageIndex by rememberSaveable() {
            mutableStateOf(1)
        }

        val maxPageIndex by remember {
            derivedStateOf {
                if (recipeList == null) 0
                else ceil(recipeList!!.size.div(MAX_PAGE_ITEM_NUMBER.toDouble())).toInt()
            }
        }

        DisposableEffect(maxPageIndex) {
            if (currentPageIndex > maxPageIndex) {
                currentPageIndex = maxPageIndex
            }
            onDispose { }
        }

        Column(modifier = modifier) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(recipeList?.filterIndexed { index, _ ->
                    return@filterIndexed !(index < (currentPageIndex - 1) * MAX_PAGE_ITEM_NUMBER || index >= currentPageIndex * MAX_PAGE_ITEM_NUMBER)
                }
                    ?: ArrayList()) { recipePreview ->
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

            recipeList?.let {
                if (it.size > MAX_PAGE_ITEM_NUMBER) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RecipeSearchResultPagerButtonTheme(false) {
                            Column() {
                                AnimatedVisibility(visible = currentPageIndex > 1) {
                                    Button(
                                        onClick = {
                                            if (currentPageIndex == 1) return@Button
                                            currentPageIndex -= 1
                                        },
                                        shape = RoundedCornerShape(50.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Previous page icon"
                                        )
                                        Text(text = "Page ${(currentPageIndex - 1).coerceAtLeast(1)}")
                                    }
                                }
                            }
                            Column() {
                                AnimatedVisibility(visible = currentPageIndex < maxPageIndex) {
                                    Button(
                                        onClick = {
                                            if (currentPageIndex == maxPageIndex) return@Button
                                            currentPageIndex += 1
                                        },
                                        shape = RoundedCornerShape(50.dp)
                                    ) {
                                        Text(
                                            text = "Page ${
                                                (currentPageIndex + 1).coerceAtMost(
                                                    maxPageIndex
                                                )
                                            }"
                                        )
                                        Icon(
                                            imageVector = Icons.Default.ArrowForward,
                                            contentDescription = "Next page icon"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }


        }
    }
}

enum class BottomSheetType {
    ADD_RECIPE,
    BOOKMARK,
    NONE,
}