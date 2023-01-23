package com.vonbrank.forkify.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.vonbrank.forkify.logic.modal.RecipePreview

object BookmarkRepository {
    private val _recipeBookmarkList = MutableLiveData<List<RecipePreview>>()
    val recipeBookmarkList: LiveData<List<RecipePreview>> =
        Transformations.map(_recipeBookmarkList) { it ->
            it
        }

    fun toggleRecipeBookmarkItem(recipePreview: RecipePreview) {

        var newBookmarkList = ArrayList<RecipePreview>()

        newBookmarkList.addAll(_recipeBookmarkList.value ?: ArrayList())

        if (newBookmarkList.find { it.id == recipePreview.id } != null) {
            newBookmarkList =
                newBookmarkList.filter { it.id != recipePreview.id } as ArrayList<RecipePreview>
        } else {
            newBookmarkList.add(recipePreview)
        }

        _recipeBookmarkList.value = newBookmarkList
    }
}