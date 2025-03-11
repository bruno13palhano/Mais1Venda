package com.bruno13palhano.data.model.shared

import com.bruno13palhano.data.model.customer.Customer

data class Order(
    val id: Long,
    val productName: String,
    val productCode: String,
    val quantity: Int,
    val unitPrice: Double,
    val off: Float,
    val totalPrice: Double,
    val customer: Customer,
    val orderDate: Long,
    val deliveryDate: Long,
    val orderStatus: OrderStatus,
    val lastModifiedTimestamp: String,
)
