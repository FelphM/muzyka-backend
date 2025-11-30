package com.alice.muzyka.service

import com.alice.muzyka.entity.Product
import com.alice.muzyka.exception.ConflictException
import com.alice.muzyka.exception.NotFoundException
import com.alice.muzyka.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun getAllProducts(): List<Product> = productRepository.findAll()

    fun getProductBySlug(slug: String): Product {
        return productRepository.findBySlug(slug) ?: throw NotFoundException("Product with slug $slug not found")
    }

    fun createProduct(product: Product): Product {
        val existingProduct = productRepository.findBySlug(product.slug)
        if (existingProduct != null) {
            throw ConflictException("Product with slug ${product.slug} already exists")
        }
        return productRepository.save(product)
    }

    fun updateProduct(id: Long, product: Product): Product {
        val existingProduct = productRepository.findById(id)
            .orElseThrow { NotFoundException("Product with id $id not found") }

        val updatedProduct = existingProduct.copy(
            name = product.name,
            artist = product.artist,
            price = product.price,
            description = product.description,
            format = product.format,
            imageUrl = product.imageUrl,
            imageAlt = product.imageAlt,
            slug = product.slug
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