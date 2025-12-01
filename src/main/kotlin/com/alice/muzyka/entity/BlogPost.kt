package com.alice.muzyka.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "BLOG_POSTS")
data class BlogPost(
    @Id
    val id: String,

    @Column(name = "banner_src", length = 2000)
    val bannerSrc: String?,

    @Column(name = "banner_alt", length = 500)
    val bannerAlt: String?,

    @Column(name = "card_title", length = 500, nullable = false)
    val cardTitle: String,

    @Column(name = "card_author", length = 255)
    val cardAuthor: String?,

    @Column(name = "card_date", nullable = false)
    val cardDate: LocalDate,

    @Column(name = "card_brief", length = 1000)
    val cardBrief: String?,

    @Column(name = "post_title", length = 500, nullable = false)
    val postTitle: String,

    @Lob // For CLOB type
    @Column(name = "post_content")
    val postContent: String?
)
