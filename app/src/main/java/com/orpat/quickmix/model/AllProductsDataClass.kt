package com.orpat.quickmix.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AllProductsDataClass(
        @SerializedName("data") val data: AllProductsData,
        @SerializedName("message") val message: String,
        @SerializedName("status") val status: Int
)

data class AllProductsData(
        @SerializedName("productList") val productList: List<Product>?,
        @SerializedName("subProductList") val subProductList: List<Product>?,
        @SerializedName("summary") val summary: SummaryData?
)

data class Product(
        @SerializedName("description") val description: String,
        @SerializedName("image") val image: Any,
        @SerializedName("itemId") val itemId: Int,
        @SerializedName("price") val price: Int,
        @SerializedName("subTotal") val subTotal: Int,
        @SerializedName("productId") val productId: Int,
        @SerializedName("subProductId") val subProductId: Int,
        @SerializedName("productName") val productName: String,
        @SerializedName("subText") val subText: String,
        @SerializedName("quantity") var quantity: Int,
        @SerializedName("is_accessorie") var is_accessorie: Int
) : Serializable

data class SummaryData(
        @SerializedName("totalQuantity") val totalQuantity: Int,
        @SerializedName("subtotal") val subtotal: Int
        )