package com.vonbrank.forkify.ui.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vonbrank.forkify.logic.BookmarkRepository

class BookmarkViewModal : ViewModel() {
    val recipeBookmarkList = BookmarkRepository.recipeBookmarkList

    private val _bookmarkSidebarOpen = MutableLiveData<Boolean>(false)
    val bookmarkSidebarOpen: LiveData<Boolean> = _bookmarkSidebarOpen

    fun openBookmarkSidebar() {
        _bookmarkSidebarOpen.value = true
    }

    fun closeBookmarkSidebar() {
        _bookmarkSidebarOpen.value = false
    }

    fun toggleBookmarkSidebar() {
        _bookmarkSidebarOpen.value =
            if (_bookmarkSidebarOpen.value != null) !_bookmarkSidebarOpen.value!! else false
    }
}