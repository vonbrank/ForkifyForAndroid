package com.vonbrank.forkify.ui.recipePreview

import androidx.lifecycle.ViewModel
import com.vonbrank.forkify.logic.modal.RecipePreview

class RecipePreviewActionViewModal : ViewModel() {
    var handleClickRecipePreviewItem: (recipePreview: RecipePreview) -> Unit = { }
}