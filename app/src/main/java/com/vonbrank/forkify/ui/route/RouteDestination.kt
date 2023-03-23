package com.vonbrank.forkify.ui.route

import androidx.navigation.NavController
import com.vonbrank.forkify.logic.modal.RecipePreview

interface RouteDestination {
    val route: String
}

object Home : RouteDestination {
    override val route = "home"
}

object RecipeDetail : RouteDestination {
    override val route = "recipeDetail"
    private const val RECIPE_PREVIEW = "recipe_preview"
    fun sendArguments(navController: NavController, recipePreview: RecipePreview) {
        navController.currentBackStackEntry?.savedStateHandle?.apply {
            set(RECIPE_PREVIEW, recipePreview)
        }
    }

    fun receiveArguments(
        navController: NavController,
        clearDataAfterReceived: Boolean = true,
        receiveBlock: (recipePreviewArg: RecipePreview?) -> Unit
    ) {
        val recipePreview: RecipePreview? =
            navController.previousBackStackEntry?.savedStateHandle?.get(RECIPE_PREVIEW)
        receiveBlock(recipePreview)
        if (clearDataAfterReceived)
            navController.previousBackStackEntry?.savedStateHandle?.set(
                RECIPE_PREVIEW,
                null
            )
    }
}

