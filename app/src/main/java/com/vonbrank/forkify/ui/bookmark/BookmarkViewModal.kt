package com.vonbrank.forkify.ui.bookmark

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vonbrank.forkify.logic.BookmarkRepository
import com.vonbrank.forkify.logic.modal.RecipePreview

class BookmarkViewModal : ViewModel() {
    val recipeBookmarkList = BookmarkRepository.recipeBookmarkList
}