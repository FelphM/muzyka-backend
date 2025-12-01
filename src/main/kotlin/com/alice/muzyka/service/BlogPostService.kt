package com.alice.muzyka.service

import com.alice.muzyka.entity.BlogPost
import com.alice.muzyka.repository.BlogPostRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class BlogPostService(private val blogPostRepository: BlogPostRepository) {

    fun getAllBlogPosts(): List<BlogPost> = blogPostRepository.findAll()

    fun getBlogPostById(id: String): BlogPost? = blogPostRepository.findById(id).orElse(null)

    fun createBlogPost(blogPost: BlogPost): BlogPost {
        // Ensure the ID is unique, generate if not provided or exists
        // For simplicity, let's assume the ID is provided by the client for now,
        // or we could generate a UUID here if needed.
        return blogPostRepository.save(blogPost)
    }

    fun updateBlogPost(id: String, updatedBlogPost: BlogPost): BlogPost? {
        return if (blogPostRepository.existsById(id)) {
            val blogPostToUpdate = updatedBlogPost.copy(id = id)
            blogPostRepository.save(blogPostToUpdate)
        } else {
            null
        }
    }

    fun deleteBlogPost(id: String) {
        blogPostRepository.deleteById(id)
    }
}
