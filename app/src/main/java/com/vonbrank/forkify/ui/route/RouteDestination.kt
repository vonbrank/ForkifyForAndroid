package com.vonbrank.forkify.ui.route

interface RouteDestination {
    val route: String
}

object Home : RouteDestination {
    override val route = "home"
}

object RecipeDetail : RouteDestination {
    override val route = "recipeDetail"
}

