package com.alice.muzyka.controller.v1

import com.alice.muzyka.dto.OrderRequest
import com.alice.muzyka.dto.PurchaseOrderDto
import com.alice.muzyka.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(private val orderService: OrderService) {

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

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateOrderStatus(@PathVariable id: Long, @RequestBody statusUpdate: Map<String, String>): ResponseEntity<PurchaseOrderDto> {
        val status = statusUpdate["status"] ?: throw IllegalArgumentException("Status is required")
        val updatedOrder = orderService.updateOrderStatus(id, status)
        return ResponseEntity.ok(updatedOrder)
    }
}
