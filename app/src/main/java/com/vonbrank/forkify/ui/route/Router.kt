package com.vonbrank.forkify.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vonbrank.forkify.ui.page.RecipeDetail
import com.vonbrank.forkify.ui.page.RecipeSearch

@Composable
fun Router() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Home.route) {
        composable(Home.route) {
            RecipeSearch(navController = navController)
        }

        composable(RecipeDetail.route) {
            RecipeDetail(navController = navController)
        }
    }
}