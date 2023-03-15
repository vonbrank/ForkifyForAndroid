package com.vonbrank.forkify.ui.recipePreview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vonbrank.forkify.ForkifyApplication
import com.vonbrank.forkify.logic.modal.RecipePreview
import com.vonbrank.forkify.ui.recipeDetail.RecipeDetailActivity

class RecipePreviewViewModal : ViewModel() {
    val recipePreviewList = ArrayList<RecipePreview>()
}