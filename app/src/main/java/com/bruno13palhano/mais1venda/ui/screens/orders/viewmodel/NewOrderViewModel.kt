package com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.data.notifications.EventBus
import com.bruno13palhano.data.notifications.OrderEvent
import com.bruno13palhano.data.repository.OrderRepository
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.DialogMessageType
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrderEvent
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrderSideEffect
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrderState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay

@HiltViewModel
internal class NewOrderViewModel @Inject constructor(
    initialState: NewOrderState,
    private val orderRepository: OrderRepository,
    private val eventBus: EventBus,
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
        eventBus.events.collect { eventBus ->
            when (eventBus) {
                is OrderEvent.OrderCreated -> {
                    if (eventBus.order.id == id) {
                        reduce { copy(order = eventBus.order) }
                    }
                }
                else -> {}
            }
        }
    }

    private fun confirmOrder() = container.intent {
        container.state.value.order?.id?.let {
            reduce { copy(isLoading = true) }
            val result = orderRepository.confirmOrder(id = it)

            when (result) {
                is Resource.Success -> {
                    reduce { copy(isLoading = false) }
                    postSideEffect(
                        effect = NewOrderSideEffect.ShowDialog(messageType = DialogMessageType.OK),
                    )
                    delay(1000)
                    postSideEffect(effect = NewOrderSideEffect.NavigateBack)
                }

                is Resource.ResponseError -> {
                    reduce { copy(isLoading = false) }
                    result.remoteResponseError?.let { remoteResponseError ->
                        postSideEffect(
                            effect = NewOrderSideEffect.ShowError(
                                codeError = CodeError.UNKNOWN_ERROR,
                            ),
                        )
                    }
                }

                is Resource.Error -> {
                    reduce { copy(isLoading = false) }
                    result.errorType?.let { errorType ->
                        postSideEffect(
                            effect = NewOrderSideEffect.ShowError(
                                codeError = CodeError.UNKNOWN_ERROR,
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun cancelOrder() = container.intent {
        postSideEffect(
            effect = NewOrderSideEffect.ShowDialog(messageType = DialogMessageType.CANCEL),
        )
        // TODO: Implement cancel order logic; for now, just navigate back
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
