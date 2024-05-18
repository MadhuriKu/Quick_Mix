package com.orpat.quickmix.model

data class PaymentLinkDataClass(
    val data: PaymentLinkData,
    val message: String,
    val status: Int
)

data class PaymentLinkData(
    val url: String
)