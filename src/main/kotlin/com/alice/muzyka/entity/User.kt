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
    val username: String,
    val email: String,
    val phone: String? = null,
    val address: String? = null,
    val city: String? = null,
    @Column(name = "state_province")
    val stateProvince: String? = null,
    @Column(name = "postal_code")
    val postalCode: String? = null,
    @JsonIgnore
    @Column(name = "password_hash")
    val passwordHash: String, // Store password hash, not plain password
    val role: String, // e.g., "ADMIN", "CUSTOMER"
    val status: String, // e.g., "ACTIVE", "INACTIVE"
    @Column(name = "join_date")
    val joinDate: Instant = Instant.now(),
    @Column(name = "last_login")
    var lastLogin: Instant? = null
)