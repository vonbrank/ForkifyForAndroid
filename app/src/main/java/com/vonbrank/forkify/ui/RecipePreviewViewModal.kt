package com.vonbrank.forkify.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.vonbrank.forkify.logic.Repository
import com.vonbrank.forkify.logic.modal.RecipePreview

class RecipePreviewViewModal : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    val recipeList = ArrayList<RecipePreview>()

    val recipeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchRecipe(query)
    }

    fun searchRecipe(query: String) {
        searchLiveData.value = query
    }
}