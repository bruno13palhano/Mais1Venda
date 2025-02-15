package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

import androidx.compose.runtime.Immutable

@Immutable
internal data class OrdersStatusState(
    val newOrdersCount: Int = 0,
)

@Immutable
internal sealed interface OrdersStatusEvent {
    data object LoadNewOrdersCount : OrdersStatusEvent
    data object NavigateToNewOrders : OrdersStatusEvent
    data object NavigateToOrders : OrdersStatusEvent
    data object NavigateToCustomers : OrdersStatusEvent
    data object NavigateBack : OrdersStatusEvent
}

@Immutable
internal sealed interface OrdersStatusSideEffect {
    data object NavigateToNewOrders : OrdersStatusSideEffect
    data object NavigateToOrders : OrdersStatusSideEffect
    data object NavigateToCustomers : OrdersStatusSideEffect
    data object NavigateBack : OrdersStatusSideEffect
}
