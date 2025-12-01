package com.alice.muzyka.dto

data class ChangePasswordRequest(
    val email: String,
    val currentPassword: String,
    val newPassword: String
)
