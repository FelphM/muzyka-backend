package com.alice.muzyka.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.JoinColumn

@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val artist: String,
    val name: String,
    val imageUrl: String,
    val imageAlt: String,
    val price: Double,
    val format: String,
    val description: String,
    val slug: String,
    var stock: Int,

    @ManyToOne
    @JoinColumn(name = "category_id")
    val category: Category
)