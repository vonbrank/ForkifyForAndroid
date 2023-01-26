package com.vonbrank.forkify.logic.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vonbrank.forkify.logic.modal.RecipePreview
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeBookmarkDao {
    @Query("SELECT * FROM recipe_bookmark ORDER BY id ASC")
    fun getAllRecipe(): Flow<List<RecipePreview>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipePreview: RecipePreview)

    @Query("DELETE FROM recipe_bookmark")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(recipePreview: RecipePreview)
}