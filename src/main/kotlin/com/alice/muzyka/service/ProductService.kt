package com.alice.muzyka.service

import com.alice.muzyka.entity.Product
import com.alice.muzyka.exception.ConflictException
import com.alice.muzyka.exception.NotFoundException
import com.alice.muzyka.repository.ProductRepository
import com.alice.muzyka.repository.CategoryRepository
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) {

    fun getAllProducts(): List<Product> = productRepository.findAll()

    fun getProductBySlug(slug: String): Product {
        return productRepository.findBySlug(slug) ?: throw NotFoundException("Product with slug $slug not found")
    }

    fun createProduct(product: Product): Product {
        val existingProduct = productRepository.findBySlug(product.slug)
        if (existingProduct != null) {
            throw ConflictException("Product with slug ${product.slug} already exists")
        }

        val category = product.category.id?.let {
            categoryRepository.findById(it)
                .orElseThrow { NotFoundException("Category with id ${product.category.id} not found") }
        } ?: throw IllegalArgumentException("Category ID must not be null")


        val newProduct = product.copy(category = category)
        return productRepository.save(newProduct)
    }

    fun updateProduct(id: Long, product: Product): Product {
        val existingProduct = productRepository.findById(id)
            .orElseThrow { NotFoundException("Product with id $id not found") }

        val category = product.category.id?.let {
            categoryRepository.findById(it)
                .orElseThrow { NotFoundException("Category with id ${product.category.id} not found") }
        } ?: throw IllegalArgumentException("Category ID must not be null")

        val updatedProduct = existingProduct.copy(
            name = product.name,
            artist = product.artist,
            price = product.price,
            description = product.description,
            format = product.format,
            imageUrl = product.imageUrl,
            imageAlt = product.imageAlt,
            slug = product.slug,
            stock = product.stock,
            category = category
        )
        return productRepository.save(updatedProduct)
    }

    fun deleteProduct(id: Long) {
        if (!productRepository.existsById(id)) {
            throw NotFoundException("Product with id $id not found")
        }
        productRepository.deleteById(id)
    }
}