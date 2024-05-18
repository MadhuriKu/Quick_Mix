package com.orpat.quickmix.model

data class RecipeDataClass(
    val data: List<RecipeData>,
    val message: String,
    val status: Int
)

data class RecipeData(
    val calories: String,
    val category_id: Int,
    val createdAt: Any,
    val description: String,
    val difficulty: String,
    val direction: String,
    val duration: String,
    val id: Int,
    val image: String,
    val ingredients: String,
    val is_active: Int,
    val name: String,
    val updatedAt: Any,
    val yield: String
)