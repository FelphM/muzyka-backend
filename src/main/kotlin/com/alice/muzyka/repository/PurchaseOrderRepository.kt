package com.alice.muzyka.repository

import com.alice.muzyka.entity.PurchaseOrder
import org.springframework.data.jpa.repository.JpaRepository

import com.alice.muzyka.entity.User

interface PurchaseOrderRepository : JpaRepository<PurchaseOrder, Long> {
    fun findByUser(user: User): List<PurchaseOrder>
}
