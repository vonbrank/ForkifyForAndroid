package com.vonbrank.forkify.ui.recipePreview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vonbrank.forkify.logic.modal.RecipePreview

class RecipePreviewViewModal : ViewModel() {
    val recipePreviewList = ArrayList<RecipePreview>()
}