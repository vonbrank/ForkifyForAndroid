package com.vonbrank.forkify.logic.network

import com.vonbrank.forkify.ForkifyApplication
import com.vonbrank.forkify.logic.modal.RecipeDetailResponse
import com.vonbrank.forkify.logic.modal.RecipePreviewResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeService {
    @GET("api/v2/recipes/?key=${ForkifyApplication.TOKEN}")
    fun searchRecipePreview(@Query("search") query: String): Call<RecipePreviewResponse>

    @GET("api/v2/recipes/{recipeId}?key=${ForkifyApplication.TOKEN}")
    fun getRecipeDetail(@Path("recipeId") recipeId: String): Call<RecipeDetailResponse>
}