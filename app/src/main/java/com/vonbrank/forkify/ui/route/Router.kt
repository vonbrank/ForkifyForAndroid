package com.vonbrank.forkify.ui.route

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vonbrank.forkify.ForkifyApplication
import com.vonbrank.forkify.logic.modal.RecipePreview
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

            var recipePreview: RecipePreview? by remember {
                mutableStateOf(null)
            }

            LaunchedEffect(Unit) {
                recipePreview =
                    navController.previousBackStackEntry?.savedStateHandle?.get("recipe_preview")
                if (recipePreview == null) {
                    navController.popBackStack()
                    Toast.makeText(
                        ForkifyApplication.context,
                        "Failed to navigate to recipe detail.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            recipePreview?.let { RecipeDetail(navController = navController, it) }
        }
    }
}