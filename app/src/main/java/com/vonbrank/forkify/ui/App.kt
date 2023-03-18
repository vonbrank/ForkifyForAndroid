package com.vonbrank.forkify.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.vonbrank.forkify.ui.component.SearchAppBar
import com.vonbrank.forkify.ui.page.RecipeSearch
import com.vonbrank.forkify.ui.theme.ForkifyTheme
import com.vonbrank.forkify.R

@Composable
fun App() {
    ForkifyTheme() {

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
            })
        }) { paddingValues ->
            RecipeSearch(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            )
        }
    }
}