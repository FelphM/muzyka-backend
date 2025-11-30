package com.alice.muzyka.controller.v1

import com.alice.muzyka.entity.Product
import com.alice.muzyka.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
class ProductCtrlV1(private val productService: ProductService) {

    @GetMapping
    fun getAllProducts(): List<Product> = productService.getAllProducts()

    @GetMapping("/{slug}")
    fun getProductBySlug(@PathVariable slug: String): ResponseEntity<Product> {
        val product = productService.getProductBySlug(slug)
        return ResponseEntity.ok(product)
    }

    @PostMapping
    fun createProduct(@RequestBody product: Product): ResponseEntity<Product> {
        val createdProduct = productService.createProduct(product)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct)
    }

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @RequestBody product: Product): ResponseEntity<Product> {
        val updatedProduct = productService.updateProduct(id, product)
        return ResponseEntity.ok(updatedProduct)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }
}