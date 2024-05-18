package com.orpat.quickmix.model



data class StateDataClass(
    val data: List<StateData>,
    val message: String,
    val status: Int
)

data class asd(
    val country: String,
    val country_code: String,
    val date_of_birth: String,
    val email: String,
    val gender: String,
    val goals: List<String>,
    val isFacebook: Boolean,
    val isGoogle: Boolean,
    val level: String,
    val login_type: Any,
    val message: String,
    val mobile: String,
    val name: String,
    val status_code: Int,
    val title: String,
    val user_id: String,
    val user_type: String
)

data class StateData(
    val id: Int,
    val name: String
){
    override fun toString(): String {
        return name
    }
}


