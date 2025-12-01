package com.alice.muzyka.service

import com.alice.muzyka.dto.OrderItemDto
import com.alice.muzyka.dto.OrderRequest
import com.alice.muzyka.dto.PurchaseOrderDto
import com.alice.muzyka.entity.OrderItem
import com.alice.muzyka.entity.Product
import com.alice.muzyka.entity.PurchaseOrder
import com.alice.muzyka.exception.NotFoundException
import com.alice.muzyka.repository.ProductRepository
import com.alice.muzyka.repository.PurchaseOrderRepository
import com.alice.muzyka.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class OrderService(
    private val purchaseOrderRepository: PurchaseOrderRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun createOrder(orderRequest: OrderRequest): PurchaseOrderDto {
        val user = userRepository.findById(orderRequest.userId)
            .orElseThrow { NotFoundException("User with id ${orderRequest.userId} not found") }

        val preliminaryOrderItems = mutableListOf<Triple<Product, Int, BigDecimal>>()
        var totalPrice = BigDecimal.ZERO

        for (itemRequest in orderRequest.items) {
            val product = productRepository.findById(itemRequest.productId)
                .orElseThrow { NotFoundException("Product with id ${itemRequest.productId} not found") }

            if (product.stock < itemRequest.quantity) {
                throw IllegalStateException("Not enough stock for product ${product.name}. Available: ${product.stock}, Requested: ${itemRequest.quantity}")
            }

            // Decrement stock
            product.stock -= itemRequest.quantity
            productRepository.save(product)

            val itemPrice = BigDecimal.valueOf(product.price)
            preliminaryOrderItems.add(Triple(product, itemRequest.quantity, itemPrice))
            totalPrice += itemPrice.multiply(BigDecimal.valueOf(itemRequest.quantity.toLong()))
        }

        val newOrder = PurchaseOrder(
            user = user,
            totalPrice = totalPrice,
            status = "PENDING"
        )

        val orderItems = preliminaryOrderItems.map { (product, quantity, price) ->
            OrderItem(
                purchaseOrder = newOrder,
                product = product,
                quantity = quantity,
                price = price
            )
        }

        newOrder.items.addAll(orderItems)

        val savedOrder = purchaseOrderRepository.save(newOrder)
        return toDto(savedOrder)
    }

    @Transactional(readOnly = true)
    fun getAllOrders(): List<PurchaseOrderDto> {
        return purchaseOrderRepository.findAll().map { toDto(it) }
    }

    @Transactional
    fun updateOrderStatus(orderId: Long, status: String): PurchaseOrderDto {
        val order = purchaseOrderRepository.findById(orderId)
            .orElseThrow { NotFoundException("Order with id $orderId not found") }

        order.status = status
        val savedOrder = purchaseOrderRepository.save(order)
        return toDto(savedOrder)
    }

    private fun toDto(order: PurchaseOrder): PurchaseOrderDto {
        return PurchaseOrderDto(
            id = order.id,
            orderDate = order.orderDate,
            totalPrice = order.totalPrice,
            status = order.status,
            userName = order.user.name,
            items = order.items.map {
                OrderItemDto(
                    id = it.id,
                    productId = it.product.id,
                    productName = it.product.name,
                    quantity = it.quantity,
                    price = it.price
                )
            }
        )
    }
}
