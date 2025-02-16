package com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.data.repository.OrderRepository
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrdersEvent
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrdersSideEffect
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrdersState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class NewOrdersViewModel @Inject constructor(
    initialState: NewOrdersState,
    private val orderRepository: OrderRepository,
) : ViewModel() {
    val container = Container<NewOrdersState, NewOrdersSideEffect>(
        initialState = initialState,
        scope = viewModelScope,
    )

    fun handleEvent(event: NewOrdersEvent) {
        when (event) {
            is NewOrdersEvent.LoadNewOrders -> {}

            is NewOrdersEvent.NavigateToNewOrder -> {}

            NewOrdersEvent.NavigateBack -> {}
        }
    }

    private fun loadNewOrders() = container.intent {
        orderRepository.getAll().collect { orders ->
            reduce { copy(newOrders = orders) }
        }
    }
}
