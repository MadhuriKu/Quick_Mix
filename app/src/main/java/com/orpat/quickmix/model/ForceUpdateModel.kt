package com.orpat.quickmix.model

import com.google.gson.annotations.SerializedName

/*
data class ForceUpdateModel(
    val code: Int,
    val force_update: Boolean
)*/
data class ForceUpdateModel(
@SerializedName("data") val data: Data,
@SerializedName("message") val message: String,
@SerializedName("status") val status: Int
)

data class Data(
    @SerializedName("code") val code: Int,
    @SerializedName("force_update") val force_update: Boolean
)

