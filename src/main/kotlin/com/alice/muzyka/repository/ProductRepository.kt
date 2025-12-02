package com.alice.muzyka.repository

import com.alice.muzyka.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findBySlug(slug: String): Product?
    fun findBySlugAndDeletedFalse(slug: String): Product?
    fun findAllByDeletedFalse(): List<Product>
    fun findByCategoryIdAndDeletedFalse(categoryId: Long): List<Product>
}