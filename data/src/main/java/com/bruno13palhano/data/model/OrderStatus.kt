package com.bruno13palhano.data.model

enum class OrderStatus {
    AWAITING_PAYMENT,
    PROCESSING_PAYMENT,
    PAYMENT_FAILED,
    PAYMENT_CONFIRMED,
    PROCESSING_ORDER,
    AWAITING_DELIVERY,
    AWAITING_CONFIRMATION,
    AWAITING_PICKUP,
    DELIVERED,
    PICKED_UP,
    CANCELED,
    FINISHED,
}
