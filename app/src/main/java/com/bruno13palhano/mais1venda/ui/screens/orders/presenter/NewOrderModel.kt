package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

import androidx.compose.runtime.Immutable

@Immutable
internal data class NewOrderState(
    val id: Long = 0L,
)

@Immutable
internal sealed interface NewOrderEvent {
    data class LoadOrder(val id: Long) : NewOrderEvent
    data object NavigateBack : NewOrderEvent
}

@Immutable
internal sealed interface NewOrderSideEffect {
    data object NavigateBack : NewOrderSideEffect
}
