package com.vonbrank.forkify.logic.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vonbrank.forkify.logic.modal.RecipePreview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(RecipePreview::class), version = 1, exportSchema = false)
public abstract class BookmarkRoomDatabase : RoomDatabase() {
    abstract fun recipeBookmarkDao(): RecipeBookmarkDao

    private class BookmarkDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
                scope.launch {

                }
            }
        }
    }

    companion object {
        private var INSTANCE: BookmarkRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BookmarkRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookmarkRoomDatabase::class.java,
                    "recipe_database"
                ).addCallback(BookmarkDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }
}