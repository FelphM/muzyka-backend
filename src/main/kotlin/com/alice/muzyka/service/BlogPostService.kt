package com.alice.muzyka.service

import com.alice.muzyka.entity.BlogPost
import com.alice.muzyka.repository.BlogPostRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import com.alice.muzyka.dto.BlogPostCreateRequest

@Service
class BlogPostService(private val blogPostRepository: BlogPostRepository) {

    fun getAllBlogPosts(): List<BlogPost> = blogPostRepository.findAll()

    fun getBlogPostById(id: String): BlogPost? = blogPostRepository.findById(id).orElse(null)

    fun createBlogPost(request: BlogPostCreateRequest): BlogPost {
        // Generate a unique ID on the backend
        val slug = request.cardTitle.lowercase()
            .replace(Regex("[^a-z0-9\\s-]"), "")
            .replace(Regex("[\\s-]+"), "-")
        val randomSuffix = (1..6).map { (('a'..'z') + ('0'..'9')).random() }.joinToString("")
        val uniqueId = "$slug-$randomSuffix"

        val newBlogPost = BlogPost(
            id = uniqueId,
            bannerSrc = request.bannerSrc,
            bannerAlt = request.bannerAlt,
            cardTitle = request.cardTitle,
            cardAuthor = request.cardAuthor,
            cardDate = request.cardDate,
            cardBrief = request.cardBrief,
            postTitle = request.postTitle,
            postContent = request.postContent
        )
        
        return blogPostRepository.save(newBlogPost)
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

