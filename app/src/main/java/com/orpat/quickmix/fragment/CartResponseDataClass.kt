package com.orpat.quickmix.fragment

data class CartResponseDataClass(
    val data: CartResponseData,
    val message: String,
    val status: Int
)

data class CartResponseData(
    val accessoriesId: Int,
    val createdAt: String,
    val id: Int,
    val price: Int,
    val quantity: Int,
    val tokenId: Int,
    val updatedAt: String
)