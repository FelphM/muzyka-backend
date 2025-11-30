package com.alice.muzyka.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val artist: String,
    val name: String,
    val imageUrl: String,
    val imageAlt: String,
    val price: Double,
    val format: String,
    val description: String,
    val slug: String
)