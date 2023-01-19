package com.vonbrank.forkify.logic.modal

import com.google.gson.annotations.SerializedName

data class Recipe(
    val id: String,
    @SerializedName("image_url") val imageUrl: String,
    val title: String,
    val publisher: String
)
