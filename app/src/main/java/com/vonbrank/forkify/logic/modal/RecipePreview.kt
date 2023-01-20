package com.vonbrank.forkify.logic.modal

import com.google.gson.annotations.SerializedName

data class RecipePreviewResponse(
    val status: String,
    val result: Number,
    val data: RecipePreviewResponseData
)

data class RecipePreviewResponseData(val recipes: List<RecipePreview>)

data class RecipePreview(
    val id: String,
    @SerializedName("image_url") val imageUrl: String,
    val title: String,
    val publisher: String
)
