package com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersStatusEvent
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersStatusSideEffect
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersStatusState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class OrdersStatusViewModel @Inject constructor(
    initialState: OrdersStatusState,
) : ViewModel() {
    val container = Container<OrdersStatusState, OrdersStatusSideEffect>(
        initialState = initialState,
        scope = viewModelScope,
    )

    fun handleEvent(event: OrdersStatusEvent) {
        when (event) {
            OrdersStatusEvent.LoadNewOrdersCount -> {}

            OrdersStatusEvent.NavigateToNewOrders -> {}

            OrdersStatusEvent.NavigateToOrders -> {}

            OrdersStatusEvent.NavigateToCustomers -> {}

            OrdersStatusEvent.NavigateToDashboard -> {}

            OrdersStatusEvent.NavigateBack -> {}
        }
    }
}
