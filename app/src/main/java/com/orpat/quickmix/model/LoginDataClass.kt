package com.orpat.quickmix.model

data class LoginDataClass(
    val data: LoginData,
    val message: String,
    val status: Int
)

data class LoginData(
    val code: Int,
    val isUserRegistered: Boolean,
    val message: String
)