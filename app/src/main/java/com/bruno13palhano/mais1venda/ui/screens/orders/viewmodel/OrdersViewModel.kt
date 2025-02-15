package com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersEvent
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersSideEffect
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class OrdersViewModel @Inject constructor(
    initialStates: OrdersState,
) : ViewModel() {
    val container = Container<OrdersState, OrdersSideEffect>(
        initialState = initialStates,
        scope = viewModelScope,
    )

    fun handleEvent(event: OrdersEvent) {
        when (event) {
            is OrdersEvent.LoadOrders -> {}

            OrdersEvent.NavigateBack -> {}
        }
    }
}
