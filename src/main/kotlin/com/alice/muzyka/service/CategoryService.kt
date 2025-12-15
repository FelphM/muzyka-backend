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

    fun getAllCategories(): List<Category> = categoryRepository.findAll()

    fun getCategoryById(id: Long): Category {
        return categoryRepository.findById(id)
            .orElseThrow { NotFoundException("Category with id $id not found") }
    }

    fun getCategoryByName(name: String): Category {
        return categoryRepository.findByName(name) ?: throw NotFoundException("Category with name $name not found")
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
        
        val productsInCategory = productRepository.findByCategoryId(id)
        if (productsInCategory.isNotEmpty()) {
            val productNames = productsInCategory.map { it.name }
            throw ConflictException("Category with id $id has associated products (active or inactive) and cannot be deleted.", productNames)
        }

        categoryRepository.delete(category)
    }
}