package com.vonbrank.forkify.logic.dao

import androidx.lifecycle.asLiveData
import com.vonbrank.forkify.ForkifyApplication
import com.vonbrank.forkify.logic.modal.RecipePreview

object BookmarkDatabase {
    private val bookmarkRoomDatabase by lazy {
        BookmarkRoomDatabase.getDatabase(
            ForkifyApplication.context,
            ForkifyApplication.application.applicationScope
        )
    }

    private val bookmarkDao by lazy { bookmarkRoomDatabase.recipeBookmarkDao() }

    val allRecipes by lazy { bookmarkDao.getAllRecipe().asLiveData() }

    suspend fun insertRecipe(recipePreview: RecipePreview) {
        bookmarkDao.insert(recipePreview)
    }

    suspend fun deleteRecipe(recipePreview: RecipePreview) {
        bookmarkDao.delete(recipePreview)
    }

    suspend fun deleteAllRecipe() {
        bookmarkDao.deleteAll()
    }
}