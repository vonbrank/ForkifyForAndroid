package com.vonbrank.forkify.ui.recipeDetail

import androidx.lifecycle.*
import com.vonbrank.forkify.logic.BookmarkRepository
import com.vonbrank.forkify.logic.Repository
import com.vonbrank.forkify.logic.modal.Ingredient
import com.vonbrank.forkify.logic.modal.RecipeDetail
import com.vonbrank.forkify.logic.modal.RecipePreview

class RecipeDetailViewModal : ViewModel() {
    private val recipeIdLiveData = MutableLiveData<String>()

    val recipeDetailDefaultValue = RecipeDetail(
        0.0, "", "",
        ArrayList<Ingredient>(), "", 0, "", ""
    )

    var recipeDetail = MutableLiveData<RecipeDetail>(recipeDetailDefaultValue.copy())

    val servingsDefaultValue = 4

    val servings = MutableLiveData<Int>(servingsDefaultValue)

    var recipePreview: RecipePreview? = null

    val recipeDetailLiveData = Transformations.switchMap(recipeIdLiveData) { recipeId ->
        Repository.getRecipeDetail(recipeId)
    }

    fun getRecipeDetail(recipeId: String) {
        recipeIdLiveData.value = recipeId
    }

    val recipeBookmarkList = BookmarkRepository.recipeBookmarkList

    fun toggleRecipeBookmarkItem(recipePreview: RecipePreview) {
        BookmarkRepository.toggleRecipeBookmarkItem(recipePreview)
    }

    var independentActivity = true

}