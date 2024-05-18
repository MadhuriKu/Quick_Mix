package com.orpat.quickmix.model

data class OrderSummaryDataClass(
    val data: OrderSummaryData,
    val message: String,
    val status: Int
)

data class OrderSummaryData(
    val checkoutId: Int,
    val productList: List<OrderSummaryProductData>,
    val subProductList: List<OrderSummaryProductData>,
    val shippingAddress: ShippingAddress,
    val summary: Summary,
    val couponDetails: DiscountCouponData?,
)

data class OrderSummaryProductData(
    val image: String,
    val itemId: Int,
    val price: Double,
    val productId: Int,
    val productName: String,
    val quantity: Int,
    val subText: String,
    val subTotal: Double
)

data class ShippingAddress(
    val address: String
)

data class Summary(
    val CGST: String?,
    val SGST: String?,
    val IGST: String?,
    val GSTPrice: Double,
    val netAmount: Double,
    val getDiscountedPrice: Double,
    val grossPriceWithDiscount: Double,
    val couponCode: String?,
    val grossPrice: Double,
    val totalQuantity: Int
)