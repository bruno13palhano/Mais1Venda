package com.bruno13palhano.data.notifications

import com.bruno13palhano.data.model.shared.Order

sealed interface OrderEvent {
    data class OrderCreated(val order: Order) : OrderEvent
    data class OrderUpdated(val orderId: Long, val status: String) : OrderEvent
    data class OrderDeleted(val orderId: Long) : OrderEvent
}
