package com.alice.muzyka.dto

import java.time.LocalDate

// DTO for updating a blog post.
data class BlogPostUpdateRequest(
    val bannerSrc: String?,
    val bannerAlt: String?,
    val cardTitle: String,
    val cardAuthor: String?,
    val cardDate: LocalDate,
    val cardBrief: String?,
    val postTitle: String,
    val postContent: String?
)
