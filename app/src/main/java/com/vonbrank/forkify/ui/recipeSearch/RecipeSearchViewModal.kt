package com.vonbrank.forkify.ui.recipeSearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.vonbrank.forkify.logic.Repository
import com.vonbrank.forkify.logic.modal.RecipePreview

class RecipeSearchViewModal : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    val recipeListLivaData = MutableLiveData<ArrayList<RecipePreview>>()

    val recipeListPaginationNumber = MutableLiveData<Int>(1)

    val recipeLoadResultLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchRecipe(query)
    }

    fun searchRecipe(query: String) {
        searchLiveData.value = query
    }
}