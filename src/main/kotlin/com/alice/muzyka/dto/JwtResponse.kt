package com.alice.muzyka.dto

import com.alice.muzyka.entity.User

data class JwtResponse(
    val token: String,
    val user: User
)