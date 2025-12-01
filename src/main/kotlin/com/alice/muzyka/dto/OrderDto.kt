package com.alice.muzyka.dto

import java.math.BigDecimal
import java.time.Instant

data class OrderItemDto(
    val id: Long?,
    val productId: Long?,
    val productName: String?,
    val quantity: Int,
    val price: BigDecimal
)

data class PurchaseOrderDto(
    val id: Long?,
    val orderDate: Instant,
    val totalPrice: BigDecimal,
    val status: String,
    val userName: String?,
    val items: List<OrderItemDto>
)
