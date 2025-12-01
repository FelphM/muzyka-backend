package com.alice.muzyka.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "purchase_orders")
data class PurchaseOrder(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val orderDate: Instant = Instant.now(),

    @Column(nullable = false)
    val totalPrice: BigDecimal,

    @Column(nullable = false)
    var status: String, // e.g., "PENDING", "COMPLETED", "CANCELLED"

    @OneToMany(mappedBy = "purchaseOrder", cascade = [CascadeType.ALL], orphanRemoval = true)
    val items: MutableList<OrderItem> = mutableListOf()
)
