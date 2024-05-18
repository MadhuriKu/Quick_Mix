package com.orpat.quickmix.model

import com.google.gson.annotations.SerializedName

data class AddressDataClass(
        @SerializedName("data") val data: String,
        @SerializedName("message") val message: String,
        @SerializedName("status") val status: Int
)
