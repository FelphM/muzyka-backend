package com.alice.muzyka.repository

import com.alice.muzyka.entity.PurchaseOrder
import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseOrderRepository : JpaRepository<PurchaseOrder, Long>
