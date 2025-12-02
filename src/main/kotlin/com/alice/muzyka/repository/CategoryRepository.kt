package com.alice.muzyka.repository

import com.alice.muzyka.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    fun findByName(name: String): Category?
    fun findAllByDeletedFalse(): List<Category>
    fun findByNameAndDeletedFalse(name: String): Category?
}