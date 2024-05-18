package com.orpat.quickmix.model

import com.google.gson.annotations.SerializedName

data class TokenDataClass(
    @SerializedName("data") val data: TokenData,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)

data class TokenData(
    @SerializedName("token") val token: String
)