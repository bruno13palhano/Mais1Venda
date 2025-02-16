package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

import androidx.compose.runtime.Immutable

@Immutable
internal data class OrdersDashboardState(
    val amount: Float = 0f,
)

@Immutable
internal sealed interface OrdersDashboardEvent {
    data object NavigateBack : OrdersDashboardEvent
}

@Immutable
internal sealed interface OrdersDashboardSideEffect {
    data object NavigateBack : OrdersDashboardSideEffect
}
