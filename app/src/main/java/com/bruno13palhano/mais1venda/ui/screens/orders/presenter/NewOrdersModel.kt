package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.data.model.shared.Order

@Immutable
internal data class NewOrdersState(
    val newOrders: List<Order> = emptyList()
)

@Immutable
internal sealed interface NewOrdersEvent {
    data class LoadNewOrders(val newOrders: List<Order>) : NewOrdersEvent
    data object NavigateBack : NewOrdersEvent
    data class NavigateToNewOrder(val id: Long) : NewOrdersEvent
}

@Immutable
internal sealed interface NewOrdersSideEffect {
    data class NavigateToNewOrder(val id: Long) : NewOrdersSideEffect
    data object NavigateBack : NewOrdersSideEffect
}
