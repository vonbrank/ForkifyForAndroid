package com.vonbrank.forkify.logic.network

import android.util.Log
import com.vonbrank.forkify.logic.modal.RecipeDetailResponse
import com.vonbrank.forkify.logic.modal.RecipePreviewResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ForkifyNetwork {
    private val recipeService = ServiceCreator.create<RecipeService>()

    suspend fun searchRecipe(query: String): RecipePreviewResponse {
        return recipeService.searchRecipePreview(query).await()
    }

    suspend fun getRecipeDetail(recipeId: String): RecipeDetailResponse {
        return recipeService.getRecipeDetail(recipeId).await()
    }

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}