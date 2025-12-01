package com.alice.muzyka.controller.v1

import com.alice.muzyka.entity.BlogPost
import com.alice.muzyka.service.BlogPostService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/blogposts")
class BlogPostCtrlV1(private val blogPostService: BlogPostService) {

    @GetMapping
    fun getAllBlogPosts(): ResponseEntity<List<BlogPost>> {
        val blogPosts = blogPostService.getAllBlogPosts()
        return ResponseEntity.ok(blogPosts)
    }

    @GetMapping("/{id}")
    fun getBlogPostById(@PathVariable id: String): ResponseEntity<BlogPost> {
        val blogPost = blogPostService.getBlogPostById(id)
        return if (blogPost != null) {
            ResponseEntity.ok(blogPost)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createBlogPost(@RequestBody blogPost: BlogPost): ResponseEntity<BlogPost> {
        val createdBlogPost = blogPostService.createBlogPost(blogPost)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBlogPost)
    }

    @PutMapping("/{id}")
    fun updateBlogPost(@PathVariable id: String, @RequestBody blogPost: BlogPost): ResponseEntity<BlogPost> {
        val updatedBlogPost = blogPostService.updateBlogPost(id, blogPost)
        return if (updatedBlogPost != null) {
            ResponseEntity.ok(updatedBlogPost)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteBlogPost(@PathVariable id: String): ResponseEntity<Void> {
        blogPostService.deleteBlogPost(id)
        return ResponseEntity.noContent().build()
    }
}
