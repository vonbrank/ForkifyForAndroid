package com.vonbrank.forkify.ui.recipeDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.vonbrank.forkify.logic.Repository
import com.vonbrank.forkify.logic.modal.Ingredient
import com.vonbrank.forkify.logic.modal.RecipeDetail

class RecipeDetailViewModal : ViewModel() {
    private val recipeIdLiveData = MutableLiveData<String>()

    val recipeDetailDefaultValue = RecipeDetail(
        0.0, "", "",
        ArrayList<Ingredient>(), "", 0, "", ""
    )

    var recipeDetail = MutableLiveData<RecipeDetail>(recipeDetailDefaultValue.copy())

    val servingsDefaultValue = 4

    var servings = MutableLiveData<Int>(servingsDefaultValue)

    val recipeDetailLiveData = Transformations.switchMap(recipeIdLiveData) { recipeId ->
        Repository.getRecipeDetail(recipeId)
    }

    fun getRecipeDetail(recipeId: String) {
        recipeIdLiveData.value = recipeId
    }


}