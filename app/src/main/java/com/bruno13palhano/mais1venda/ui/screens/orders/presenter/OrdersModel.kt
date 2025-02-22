package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.data.model.shared.Order

@Immutable
internal data class OrdersState(
    val orders: List<Order> = emptyList(),
    val selectedOrder: Order? = null,
    val showOrderInfo: Boolean = false,
    val openOptionMenu: Boolean = false,
)

@Immutable
internal sealed interface OrdersEvent {
    data object LoadOrders : OrdersEvent
    data class OrderInfo(val order: Order) : OrdersEvent
    data object ToggleShowOrderInfo : OrdersEvent
    data object ToggleOptionMenu : OrdersEvent
    data class UpdateSelectedOption(val option: OrdersMenuItems) : OrdersEvent
    data object NavigateBack : OrdersEvent
}

@Immutable
internal sealed interface OrdersSideEffect {
    data object NavigateBack : OrdersSideEffect
}

internal enum class OrdersMenuItems {
    SORT_BY_PRODUCT_NAME,
    SORT_BY_CUSTOMER_NAME,
    SORT_BY_PRICE,
    SORT_BY_QUANTITY,
    SORT_BY_ORDER_DATE,
    SORT_BY_DELIVERY_DATE,
}
