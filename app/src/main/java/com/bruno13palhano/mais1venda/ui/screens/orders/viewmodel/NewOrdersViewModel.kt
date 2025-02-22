package com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.model.resource.Resource
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
            is NewOrdersEvent.LoadNewOrders -> loadNewOrders()

            is NewOrdersEvent.NavigateToNewOrder -> navigateToNewOrder(id = event.id)

            NewOrdersEvent.NavigateBack -> navigateBack()
        }
    }

    private fun loadNewOrders() = container.intent {
        when (val result = orderRepository.getNewOrders()) {
            is Resource.Success -> {
                result.data?.let {
                    reduce { copy(newOrders = it) }
                }
            }

            is Resource.ResponseError -> {
                postSideEffect(
                    effect = NewOrdersSideEffect.ShowResponseError(
                        remoteError = result.remoteResponseError
                    )
                )
            }

            is Resource.Error -> {
                postSideEffect(
                    effect = NewOrdersSideEffect.ShowError(errorType = result.errorType)
                )
            }
        }
    }

    private fun navigateToNewOrder(id: Long) = container.intent {
        postSideEffect(effect = NewOrdersSideEffect.NavigateToNewOrder(id = id))
    }

    private fun navigateBack() = container.intent {
        postSideEffect(effect = NewOrdersSideEffect.NavigateBack)
    }
}
