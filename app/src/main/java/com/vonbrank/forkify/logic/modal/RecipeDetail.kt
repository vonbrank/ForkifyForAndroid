package com.vonbrank.forkify.logic.modal

import com.google.gson.annotations.SerializedName

data class RecipeDetailResponse(
    val status: String,
    val data: RecipeResponseData<RecipeDetail>
)

data class RecipeDetail(
    @SerializedName("cooking_time") val cookingTime: Number,
    val id: String,
    @SerializedName("image_url") val imageUrl: String,
    val ingredients: List<Ingredient>,
    val publisher: String,
    val servings: Number,
    @SerializedName("source_url") val sourceUrl: String,
    val title: String
)

data class Ingredient(val description: String, val quantity: Number, val unit: String)
