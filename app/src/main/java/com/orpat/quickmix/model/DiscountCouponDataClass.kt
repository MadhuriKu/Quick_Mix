package com.orpat.quickmix.model

data class DiscountCouponDataClass(
    val data: List<DiscountCouponData>,
    val message: String,
    val status: Int
)

data class DiscountCouponData(
    val couponCode: String?,
    val couponType: Int,
    val createdAt: Any,
    val description: String,
    val expiryDate: String,
    val id: Int,
    val is_active: Int,
    val maxCount: Int,
    val name: String,
    val redeemValue: Int,
    val title: String,
    val updatedAt: Any
)