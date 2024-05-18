package com.orpat.quickmix.model

import com.google.gson.annotations.SerializedName

data class TimeSlotDataClass(
        @SerializedName("data") val data: List<TimeSlotData>,
        @SerializedName("message") val message: String,
        @SerializedName("status") val status: Int
)

data class TimeSlotData(
        @SerializedName("endTime") val endTime: String,
        @SerializedName("id") val id: Int,
        @SerializedName("startTime") val startTime: String
)