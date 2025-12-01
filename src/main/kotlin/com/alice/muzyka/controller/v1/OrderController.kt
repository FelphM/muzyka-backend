package com.alice.muzyka.controller.v1

import com.alice.muzyka.dto.OrderRequest
import com.alice.muzyka.dto.PurchaseOrderDto
import com.alice.muzyka.service.OrderService
import com.alice.muzyka.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val orderService: OrderService,
    private val userService: UserService
) {
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    fun createOrder(@RequestBody orderRequest: OrderRequest): ResponseEntity<PurchaseOrderDto> {
        val order = orderService.createOrder(orderRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(order)
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllOrders(): ResponseEntity<List<PurchaseOrderDto>> {
        val orders = orderService.getAllOrders()
        return ResponseEntity.ok(orders)
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    fun getOrdersByUserId(@PathVariable userId: Long): ResponseEntity<List<PurchaseOrderDto>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication.principal

        if (principal is UserDetails) {
            val authenticatedUser = userService.findByEmail(principal.username)
            if (authenticatedUser?.id != userId) {
                throw AccessDeniedException("You are not authorized to view orders for this user.")
            }
        } else {
            throw AccessDeniedException("Authentication principal is not UserDetails.")
        }
        
        val orders = orderService.getOrdersByUserId(userId)
        return ResponseEntity.ok(orders)
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateOrderStatus(@PathVariable id: Long, @RequestBody statusUpdate: Map<String, String>): ResponseEntity<PurchaseOrderDto> {
        val status = statusUpdate["status"] ?: throw IllegalArgumentException("Status is required")
        val updatedOrder = orderService.updateOrderStatus(id, status)
        return ResponseEntity.ok(updatedOrder)
    }
}
