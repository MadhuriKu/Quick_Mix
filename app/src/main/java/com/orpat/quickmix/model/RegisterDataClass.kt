package com.orpat.quickmix.model

data class RegisterDataClass(
    val data: RegisterData,
    val message: String,
    val status: Int
)

data class RegisterData(
    val getCustomer: GetCustomer
)


