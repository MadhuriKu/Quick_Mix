package com.orpat.quickmix.model

import com.google.gson.annotations.SerializedName

data class VerifyOtpDataClass(
    @SerializedName("data") val data: VerifyOtpData,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)

data class VerifyOtpData(
    @SerializedName("data") val data: DataX,
    @SerializedName("message") val message: String
)

data class DataX(
    @SerializedName("getCustomer") val getCustomer: GetCustomer?,
    @SerializedName("isUserRegistered") val isUserRegistered: Boolean,
    @SerializedName("isDemoRegistered") val isDemoRegistered: Boolean?
)

data class GetCustomer(
    @SerializedName("address1") val address1: String,
    @SerializedName("address2") val address2: String,
    @SerializedName("cityId") val cityId: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("email") val email: String,
    @SerializedName("id") val id: Int,
    @SerializedName("is_active") val is_active: Int,
    @SerializedName("mobileNo") val mobileNo: Long,
    @SerializedName("name") val name: String,
    @SerializedName("stateId") val stateId: Int,
    @SerializedName("token") val token: String,
    @SerializedName("tokenId") val tokenId: Int,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("zipCode") val zipCode: Int
)