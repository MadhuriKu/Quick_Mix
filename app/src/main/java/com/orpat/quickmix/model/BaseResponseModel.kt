package com.orpat.quickmix.model

import com.google.gson.annotations.SerializedName

class BaseResponseModel<T> {
    @SerializedName("success")
    val success: Boolean? = null

    @SerializedName("message")
    val message: String? = null

    @SerializedName("data")
    val data: T? = null
}


data class DefaultDataModel (
    val success: Boolean,
    val message: String,
)
