package com.bruno13palhano.data.model

data class Order(
    val id: Long,
    val product: Product,
    val customer: Customer,
    val orderDate: Long,
    val deliveryDate: Long,
    val status: OrderStatus,
    val timestamp: Long,
)
