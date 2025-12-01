package com.alice.muzyka.dto

data class UserUpdateRequest(
    val username: String?,
    val email: String?,
    val role: String?,
    val status: String?
)