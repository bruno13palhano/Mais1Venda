package com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.data.repository.OrderRepository
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrderEvent
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrderSideEffect
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrderState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class NewOrderViewModel @Inject constructor(
    initialState: NewOrderState,
    private val orderRepository: OrderRepository,
) : ViewModel() {
    val container = Container<NewOrderState, NewOrderSideEffect>(
        initialState = initialState,
        scope = viewModelScope,
    )

    fun handleEvent(event: NewOrderEvent) {
        when (event) {
            is NewOrderEvent.LoadOrder -> loadOrder(id = event.id)

            NewOrderEvent.ConfirmOrder -> confirmOrder()

            NewOrderEvent.CancelOrder -> cancelOrder()

            NewOrderEvent.ConfirmCancelOrder -> confirmCancelOrder()

            NewOrderEvent.DismissKeyboard -> dismissKeyboard()

            NewOrderEvent.NavigateBack -> navigateBack()
        }
    }

    private fun loadOrder(id: Long) = container.intent {
        val order = orderRepository.get(id = id)
        reduce { copy(order = order) }
    }

    private fun confirmOrder() = container.intent {

    }

    private fun cancelOrder() = container.intent {
        postSideEffect(effect = NewOrderSideEffect.ShowCancelDialog)
    }

    private fun confirmCancelOrder() = container.intent {
        postSideEffect(effect = NewOrderSideEffect.NavigateBack)
    }

    private fun dismissKeyboard() = container.intent {
        postSideEffect(effect = NewOrderSideEffect.DismissKeyboard)
    }

    private fun navigateBack() = container.intent {
        postSideEffect(effect = NewOrderSideEffect.NavigateBack)
    }
}
