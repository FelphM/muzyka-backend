package com.alice.muzyka.dto

data class UserUpdateRequest(
    val name: String?,
    val email: String?,
    val role: String?,
    val status: String?
)