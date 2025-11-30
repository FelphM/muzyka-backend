package com.alice.muzyka.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Table

@Entity
@Table(name = "app_users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(unique = true)
    val name: String,
    val email: String,
    @JsonIgnore
    val passwordHash: String, // Store password hash, not plain password
    val role: String, // e.g., "ADMIN", "CUSTOMER"
    val status: String, // e.g., "ACTIVE", "INACTIVE"
    val joinDate: Instant = Instant.now(),
    var lastLogin: Instant? = null
)