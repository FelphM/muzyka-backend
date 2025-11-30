package com.alice.muzyka.controller.v1

import com.alice.muzyka.entity.Category
import com.alice.muzyka.service.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/categories")
class CategoryCtrlV1(private val categoryService: CategoryService) {

    @GetMapping
    fun getAllCategories(): List<Category> = categoryService.getAllCategories()

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Long): ResponseEntity<Category> {
        val category = categoryService.getCategoryById(id)
        return ResponseEntity.ok(category)
    }

    @GetMapping("/name/{name}")
    fun getCategoryByName(@PathVariable name: String): ResponseEntity<Category> {
        val category = categoryService.getCategoryByName(name)
        return ResponseEntity.ok(category)
    }

    @PostMapping
    fun createCategory(@RequestBody category: Category): ResponseEntity<Category> {
        val createdCategory = categoryService.createCategory(category)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory)
    }

    @PutMapping("/{id}")
    fun updateCategory(@PathVariable id: Long, @RequestBody category: Category): ResponseEntity<Category> {
        val updatedCategory = categoryService.updateCategory(id, category)
        return ResponseEntity.ok(updatedCategory)
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Void> {
        categoryService.deleteCategory(id)
        return ResponseEntity.noContent().build()
    }
}