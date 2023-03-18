package com.vonbrank.forkify.logic.modal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RecipePreviewResponse(
    val status: String,
    val result: Number,
    val data: RecipePreviewData
)

data class RecipePreviewData(val recipes: List<RecipePreview>)

@Entity(tableName = "recipe_bookmark")
data class RecipePreview(
    @PrimaryKey val id: String,
    @SerializedName("image_url") val imageUrl: String,
    val title: String,
    val publisher: String
) : Serializable
