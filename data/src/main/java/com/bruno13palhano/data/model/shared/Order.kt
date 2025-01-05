package com.bruno13palhano.data.model.shared

import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.customer.Customer

data class Order(
    val id: Long,
    val product: Product,
    val customer: Customer,
    val orderDate: Long,
    val deliveryDate: Long,
    val status: OrderStatus,
    val lstModifiedTimestamp: Long,
)
