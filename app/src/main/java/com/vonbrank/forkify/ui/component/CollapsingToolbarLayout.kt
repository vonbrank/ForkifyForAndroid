package com.vonbrank.forkify.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.CollapsingToolbarState

@Composable
fun CollapsingToolbarScope.CollapsingToolbarLayout(
    state: CollapsingToolbarState,
    title: String,
    expendedHeight: Dp,
    collapsedHeight: Dp = 56.dp,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(expendedHeight)
            .pin()
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(collapsedHeight)
            .pin()
    )

    val progress = state.progress
    val contentHeight = androidx.compose.ui.unit.lerp(collapsedHeight, expendedHeight, progress)
    val titleFontSize = androidx.compose.ui.unit.lerp(22.sp, 28.sp, progress)
    val titleMarginLeft = androidx.compose.ui.unit.lerp(24.dp, 0.dp, progress)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(contentHeight)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colors.primary.copy(alpha = (1 - progress))
                    )
            )
            Box(
                modifier = Modifier
                    .height(collapsedHeight)
                    .fillMaxWidth()
                    .align(Alignment.TopStart),
                contentAlignment = Alignment.CenterStart,
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back icon",
                        tint = Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .height(collapsedHeight)
                    .padding(start = (24.dp + titleMarginLeft), end = 24.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.h5.copy(fontSize = titleFontSize),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}