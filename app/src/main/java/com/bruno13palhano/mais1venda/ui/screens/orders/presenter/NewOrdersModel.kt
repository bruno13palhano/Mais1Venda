package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.data.model.resource.ErrorType
import com.bruno13palhano.data.model.resource.RemoteResponseError
import com.bruno13palhano.data.model.shared.Order

@Immutable
internal data class NewOrdersState(
    val loading: Boolean = false,
    val newOrders: List<Order> = emptyList(),
)

@Immutable
internal sealed interface NewOrdersEvent {
    data object LoadNewOrders : NewOrdersEvent
    data object NavigateBack : NewOrdersEvent
    data class NavigateToNewOrder(val id: Long) : NewOrdersEvent
}

@Immutable
internal sealed interface NewOrdersSideEffect {
    data class ShowResponseError(val remoteError: RemoteResponseError?) : NewOrdersSideEffect
    data class ShowError(val errorType: ErrorType?) : NewOrdersSideEffect
    data class NavigateToNewOrder(val id: Long) : NewOrdersSideEffect
    data object NavigateBack : NewOrdersSideEffect
}
