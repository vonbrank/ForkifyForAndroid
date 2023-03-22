package com.vonbrank.forkify.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vonbrank.forkify.ui.viewmodel.RecipeSearchViewModal

@Composable
fun SearchAppBar(
    title: @Composable () -> Unit,
    state: SearchAppBarState = rememberSearchAppBarState(),
    actions: @Composable() (RowScope.() -> Unit) = {},
    handleSearch: (query: String) -> Unit = {},
    recipeSearchViewModal: RecipeSearchViewModal = viewModel()
) {

    val loadingRecipe by recipeSearchViewModal.loadingRecipe.observeAsState()

    DisposableEffect(key1 = loadingRecipe) {
        if (!loadingRecipe!!) {
            state.searchingState = SearchAppBarSearchingState.CLOSED
        }

        onDispose { }
    }

    if (state.searchingState == SearchAppBarSearchingState.OPENED) {
        SearchWidget(
            onSearchClicked = { query -> handleSearch(query) },
            onCloseClicked = { state.searchingState = SearchAppBarSearchingState.CLOSED })
    } else {
        TopAppBar(title = title, actions = {
            IconButton(onClick = { state.searchingState = SearchAppBarSearchingState.OPENED }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon",
                    tint = Color.White
                )
            }
            actions()
        })
    }
}

enum class SearchAppBarSearchingState {
    OPENED,
    CLOSED
}

class SearchAppBarState(initialSearchingState: SearchAppBarSearchingState = SearchAppBarSearchingState.CLOSED) {
    var searchingState by mutableStateOf(initialSearchingState)

    companion object {
        val Saver: Saver<SearchAppBarState, *> = listSaver(
            save = { listOf(it.searchingState) },
            restore = {
                SearchAppBarState(initialSearchingState = it[0])
            }
        )
    }
}

@Composable
fun rememberSearchAppBarState(initialSearchingState: SearchAppBarSearchingState = SearchAppBarSearchingState.CLOSED): SearchAppBarState =
    rememberSaveable(initialSearchingState, saver = SearchAppBarState.Saver) {
        SearchAppBarState(initialSearchingState)
    }


@Composable
fun SearchWidget(
    state: SearchWidgetState = rememberSearchWidgetState(initialText = ""),
    hint: String = "Search here...",
    onSearchClicked: (value: String) -> Unit = {},
    onCloseClicked: () -> Unit = {}
) {

    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.primary,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onCloseClicked) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Close icon",
                    tint = Color.White
                )
            }
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                value = state.text,
                onValueChange = { newValue -> state.text = newValue },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
                ),
                placeholder = {
                    Text(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        text = hint,
                        color = Color.White
                    )
                },
                leadingIcon = {
                    IconButton(onClick = {
                        onSearchClicked(state.text)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search icon",
                            tint = Color.White
                        )
                    }
                },
                trailingIcon = {
                    if (state.text.isNotEmpty()) {

                        IconButton(onClick = {
                            state.text = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear icon",
                                tint = Color.White
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchClicked(state.text)
                    }
                ),
            )
        }
    }
}

class SearchWidgetState(initialText: String) {
    var text by mutableStateOf(initialText)

    companion object {
        val Saver: Saver<SearchWidgetState, *> = listSaver(
            save = { listOf(it.text) },
            restore = {
                SearchWidgetState(initialText = it[0])
            }
        )
    }
}

@Composable
fun rememberSearchWidgetState(initialText: String): SearchWidgetState =
    rememberSaveable(initialText, saver = SearchWidgetState.Saver) {
        SearchWidgetState(initialText)
    }