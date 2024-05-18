package com.orpat.quickmix.model

data class OrderDetailDataClass(
    val data: OrderDetailData,
    val message: String,
    val status: Int
)

data class OrderDetailData(
    val amount: Amount?,
    val checkoutId: Int,
    val createdAt: String,
    val customerId: Int,
    val id: Int,
    val isOrderPlaced: Int,
    val is_active: Int,
    val items: List<ODItem>,
    val orderId: Int,
    val paymentType: Int,
    val shippingAddress: ShippingAddress,
    val status: String,
    val updatedAt: String,
    val isCouponApplied: Int?,
    val couponCodeId: DiscountCouponData?,
    )

data class Amount(
        val CGST: String?,
        val SGST: String?,
        val IGST: String?,
        val GSTPrice: Double?,
        val netAmount: Double?,
        val getDiscountedPrice: Double,
        val grossPriceWithDiscount: Double,
        val couponCode: String?,
        val grossPrice: Double,
        val totalQuantity: Int
)

data class ODItem(
    val appImage: Int,
    val id: Int,
    val image: String,
    val itemId: Int,
    val name: String,
    val price: Int,
    val quantity: Int,
    val subText: String,
    val subTotal: Int,
    val webImage: Int
)

