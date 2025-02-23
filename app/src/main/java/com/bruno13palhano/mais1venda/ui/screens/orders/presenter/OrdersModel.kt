package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.data.model.shared.Order

@Immutable
internal data class OrdersState(
    val orders: List<Order> = emptyList(),
    val selectedOrder: Order? = null,
)

@Immutable
internal sealed interface OrdersEvent {
    data object LoadOrders : OrdersEvent
    data class OrderInfo(val order: Order) : OrdersEvent
    data object NavigateBack : OrdersEvent
}

@Immutable
internal sealed interface OrdersSideEffect {
    data object NavigateBack : OrdersSideEffect
}
