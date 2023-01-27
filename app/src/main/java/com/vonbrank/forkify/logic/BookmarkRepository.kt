package com.vonbrank.forkify.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import com.vonbrank.forkify.ForkifyApplication
import com.vonbrank.forkify.logic.dao.BookmarkDatabase
import com.vonbrank.forkify.logic.dao.BookmarkRoomDatabase
import com.vonbrank.forkify.logic.modal.RecipePreview
import kotlinx.coroutines.*

object BookmarkRepository {

    val recipeBookmarkList by lazy { BookmarkDatabase.allRecipes }

    fun toggleRecipeBookmarkItem(recipePreview: RecipePreview) =
        CoroutineScope(Dispatchers.IO).launch {

            val newBookmarkList = ArrayList<RecipePreview>()

            newBookmarkList.addAll(recipeBookmarkList.value ?: ArrayList())

            if (newBookmarkList.find { it.id == recipePreview.id } != null) {
                BookmarkDatabase.deleteRecipe(recipePreview)
            } else {
                BookmarkDatabase.insertRecipe(recipePreview)
            }
        }
}