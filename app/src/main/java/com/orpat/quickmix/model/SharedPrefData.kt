package com.orpat.quickmix.model


data class SharedPrefData(
    val userNumber: String?,
    val userName: String?,
    val userId: String?,
    val userEmail: String?
)

data class SharedPrefAddressData(
    val address1: String?,
    val address2: String?,
    val city: String?,
    val state: String?,
    val zipcode: String?
)
