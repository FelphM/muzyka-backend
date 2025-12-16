package com.alice.muzyka.dto

data class UserProfileUpdateRequest(
    val username: String?,
    val phone: String?,
    val address: String?,
    val city: String?,
    val stateProvince: String?,
    val postalCode: String?
)
