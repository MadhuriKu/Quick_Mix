package com.orpat.quickmix.model

data class SingleProductDetailModel(
    val data: SingleProductDetail,
    val message: String,
    val status: Int
)

data class SingleProductDetail(
    val appImage: Int,
    val createdAt: Any,
    val description: String,
    val id: Int,
    val image: String,
    val is_active: Int,
    val itemId: Int,
    val name: String,
    val price: Int,
    val priceWithoutGst: Int,
    val productId: Int,
    val quantity: Int,
    val subText: String,
    val updatedAt: String,
    val webImage: Int
)