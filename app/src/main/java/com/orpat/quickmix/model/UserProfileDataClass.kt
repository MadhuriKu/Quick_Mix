package com.orpat.quickmix.model

data class UserProfileDataClass(
    val data: UserProfileData,
    val message: String,
    val status: Int
)

data class UserProfileData(
    val address1: String,
    val address2: String,
    val cityId: Int,
    val createdAt: String,
    val email: String,
    val id: Int,
    val is_active: Int,
    val mobileNo: Long,
    val name: String,
    val stateId: Int,
    val tokenId: Int,
    val updatedAt: String,
    val zipCode: Int,
    val cityDetails: CityDetails,
    val stateDetails: StateDetails
)

data class CityDetails(
    val id: Int,
    val name: String
)

data class StateDetails(
    val id: Int,
    val name: String
)