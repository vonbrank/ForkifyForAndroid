package com.vonbrank.forkify.ui.recipeSearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.vonbrank.forkify.logic.Repository

class RecipeSearchViewModal : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    val recipeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchRecipe(query)
    }

    fun searchRecipe(query: String) {
        searchLiveData.value = query
    }
}