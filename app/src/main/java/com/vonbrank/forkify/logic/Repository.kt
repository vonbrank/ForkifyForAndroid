package com.vonbrank.forkify.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.vonbrank.forkify.logic.modal.RecipePreview
import com.vonbrank.forkify.logic.modal.RecipePreviewResponse
import com.vonbrank.forkify.logic.network.ForkifyNetwork
import kotlinx.coroutines.Dispatchers

object Repository {
    fun searchRecipe(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val recipePreviewResponse = ForkifyNetwork.searchRecipe(query)
            if (recipePreviewResponse.status == "success") {
                val recipes = recipePreviewResponse.data.recipes
                Result.success(recipes)
            } else {
                Result.failure(RuntimeException("response status is ${recipePreviewResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<RecipePreview>>(e)
        }
        emit(result)
    }
}