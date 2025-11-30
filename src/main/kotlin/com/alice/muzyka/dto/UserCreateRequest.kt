package com.alice.muzyka.dto

data class UserCreateRequest(
    val name: String,
    val email: String,
    val password: String, // Plain text password for creation
    val role: String,
    val status: String
)