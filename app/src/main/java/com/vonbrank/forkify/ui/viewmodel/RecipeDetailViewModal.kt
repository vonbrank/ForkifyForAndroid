package com.vonbrank.forkify.ui.viewmodel

import android.widget.Toast
import androidx.lifecycle.*
import com.vonbrank.forkify.ForkifyApplication
import com.vonbrank.forkify.logic.BookmarkRepository
import com.vonbrank.forkify.logic.Repository
import com.vonbrank.forkify.logic.modal.RecipeDetail
import com.vonbrank.forkify.logic.modal.RecipePreview

class RecipeDetailViewModal : ViewModel() {
    private val recipeIdLiveData = MutableLiveData<String>()

    private val recipeDetailDefaultValue = RecipeDetail(
        0.0, "", "",
        ArrayList(), "", 0, "", ""
    )

    private val servingsDefaultValue = 4

    val servings = MutableLiveData(servingsDefaultValue)

    val loadingRecipeDetail = MutableLiveData(false)

    private val recipeDetailResultLiveData = recipeIdLiveData.switchMap() { recipeId ->
        Repository.getRecipeDetail(recipeId)
    }

    val recipeDetailLiveData = recipeDetailResultLiveData.map { result ->
        val newRecipeDetail = result.getOrNull()
        loadingRecipeDetail.value = false
        if (newRecipeDetail != null) {
            servings.value = newRecipeDetail.servings
            return@map newRecipeDetail
        } else {
            Toast.makeText(
                ForkifyApplication.context,
                "Cannot load recipe detail",
                Toast.LENGTH_SHORT
            ).show()
            result.exceptionOrNull()?.printStackTrace()
            return@map recipeDetailDefaultValue.copy()
        }
    }

    fun getRecipeDetail(recipeId: String) {
        loadingRecipeDetail.value = true
        recipeIdLiveData.value = recipeId
    }

    val recipeBookmarkList = BookmarkRepository.recipeBookmarkList

    fun toggleRecipeBookmarkItem(recipePreview: RecipePreview) {
        BookmarkRepository.toggleRecipeBookmarkItem(recipePreview)
    }

    var independentActivity = true

}