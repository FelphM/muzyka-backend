package com.alice.muzyka.service

import com.alice.muzyka.entity.Category
import com.alice.muzyka.exception.ConflictException
import com.alice.muzyka.exception.NotFoundException
import com.alice.muzyka.repository.CategoryRepository
import com.alice.muzyka.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
    ) {

    fun getAllCategories(): List<Category> = categoryRepository.findAllByDeletedFalse()

    fun getCategoryById(id: Long): Category {
        val category = categoryRepository.findById(id)
            .orElseThrow { NotFoundException("Category with id $id not found") }
        if (category.deleted) {
            throw NotFoundException("Category with id $id not found")
        }
        return category
    }

    fun getCategoryByName(name: String): Category {
        return categoryRepository.findByNameAndDeletedFalse(name) ?: throw NotFoundException("Category with name $name not found")
    }

    fun createCategory(category: Category): Category {
        val existingCategory = categoryRepository.findByName(category.name)
        if (existingCategory != null) {
            throw ConflictException("Category with name ${category.name} already exists")
        }
        return categoryRepository.save(category)
    }

    fun updateCategory(id: Long, category: Category): Category {
        val existingCategory = categoryRepository.findById(id)
            .orElseThrow { NotFoundException("Category with id $id not found") }

        val categoryWithSameName = categoryRepository.findByName(category.name)
        if (categoryWithSameName != null && categoryWithSameName.id != id) {
            throw ConflictException("Category with name ${category.name} already exists")
        }

        val updatedCategory = existingCategory.copy(
            name = category.name,
            description = category.description
        )
        return categoryRepository.save(updatedCategory)
    }

    fun deleteCategory(id: Long) {
        val category = categoryRepository.findById(id)
            .orElseThrow { NotFoundException("Category with id $id not found") }
        
        val productsInCategory = productRepository.findByCategoryIdAndDeletedFalse(id)
        if (productsInCategory.isNotEmpty()) {
            val productNames = productsInCategory.map { it.name }
            throw ConflictException("Category with id $id has associated products and cannot be deleted.", productNames)
        }

        val updatedCategory = category.copy(deleted = true)
        categoryRepository.save(updatedCategory)
    }
}