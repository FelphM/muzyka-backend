package com.alice.muzyka.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Column
import jakarta.persistence.ManyToOne
import jakarta.persistence.JoinColumn

@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val artist: String,
    val name: String,
    @Column(name = "image_url")
    val imageUrl: String,
    @Column(name = "image_alt")
    val imageAlt: String,
    
    val price: Double,
    val format: String,
    val description: String,
    val slug: String,
    var stock: Int,

    @ManyToOne
    @JoinColumn(name = "category_id")
    val category: Category,

    @Column(nullable = false)
    val deleted: Boolean = false
)