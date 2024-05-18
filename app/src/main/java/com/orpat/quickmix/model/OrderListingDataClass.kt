package com.orpat.quickmix.model

data class OrderListingDataClass(
    val data: List<OrderListingData>,
    val message: String,
    val status: Int
)

data class OrderListingData(
    val amount: Double,
    val createdAt: String,
    val customerId: Int,
    val id: Int,
    val is_active: Int,
    val items: String,
    val orderId: Int,
    val status: String,
    val updatedAt: String
)