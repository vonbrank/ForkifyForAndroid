package com.vonbrank.forkify.logic.network

import com.vonbrank.forkify.ForkifyApplication
import com.vonbrank.forkify.logic.modal.RecipePreviewResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipePreviewService {
    @GET("api/v2/recipes/?key=${ForkifyApplication.TOKEN}")
    fun searchRecipePreview(@Query("search") query: String): Call<RecipePreviewResponse>
}