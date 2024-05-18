package com.orpat.quickmix.model

data class ProcessPaymentDataClass(
    val data: ProcessPaymentData,
    val message: String,
    val status: Int
)

data class ProcessPaymentData(
    val amount: Int,
    val createdAt: String,
    val customerId: Int,
    val id: Int,
    val is_active: Int,
    val items: List<Item>,
    val orderId: Int,
    val status: String,
    val updatedAt: String
)

data class Item(
    val itemId: Int,
    val price: Int,
    val quantity: Int
)