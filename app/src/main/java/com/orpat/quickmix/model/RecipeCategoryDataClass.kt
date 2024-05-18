package com.orpat.quickmix.model

data class RecipeCategoryDataClass(
    val data: List<RecipeCategoryData>,
    val message: String,
    val status: Int
)

data class RecipeCategoryData(
    val categoryId: Int,
    val categoryName: String
)