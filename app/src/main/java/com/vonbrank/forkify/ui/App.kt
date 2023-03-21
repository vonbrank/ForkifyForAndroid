package com.vonbrank.forkify.ui

import androidx.compose.runtime.Composable
import com.vonbrank.forkify.ui.route.Router
import com.vonbrank.forkify.ui.theme.ForkifyTheme

@Composable
fun App() {
    ForkifyTheme {
        Router()
    }
}