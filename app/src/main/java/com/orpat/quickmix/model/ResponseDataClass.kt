package com.orpat.quickmix.model

import com.google.gson.annotations.SerializedName

data class ResponseDataClass (
        @SerializedName("data") val data: ScheduleDemoData,
        @SerializedName("message") val message: String,
        @SerializedName("status") val status: Int
)

data class ScheduleDemoData(
    val isDemoRegistered: Boolean,
    val message: String
)