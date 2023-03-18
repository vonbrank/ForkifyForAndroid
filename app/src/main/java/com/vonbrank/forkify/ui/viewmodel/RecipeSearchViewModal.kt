package com.vonbrank.forkify.ui.viewmodel

import android.widget.Toast
import androidx.lifecycle.*
import com.vonbrank.forkify.ForkifyApplication
import com.vonbrank.forkify.logic.Repository
import com.vonbrank.forkify.logic.modal.RecipePreview

class RecipeSearchViewModal : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    val recipeListPaginationNumber = MutableLiveData<Int>(1)

    private val _loadingRecipe = MutableLiveData<Boolean>(false)

    val loadingRecipe: LiveData<Boolean>
        get() = _loadingRecipe

    private val recipeLoadResultLiveData = searchLiveData.switchMap { query ->
        Repository.searchRecipe(query)
    }

    val recipeListLivaData: LiveData<List<RecipePreview>> = recipeLoadResultLiveData.map { result ->
        val recipes = result.getOrNull()

        val res = if (recipes != null) {
            if (recipes.isEmpty()) {
                Toast.makeText(
                    ForkifyApplication.context,
                    "Cannot find out any recipe",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            ArrayList(recipes)
        } else {
            Toast.makeText(
                ForkifyApplication.context,
                "Failed to search recipe",
                Toast.LENGTH_SHORT
            ).show()
            result.exceptionOrNull()?.printStackTrace()
            ArrayList()
        }

        _loadingRecipe.value = false

        res

    }


    fun searchRecipe(query: String) {
        searchLiveData.value = query
        _loadingRecipe.value = true
    }
}