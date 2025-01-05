package com.bruno13palhano.mais1venda.ui.screens.home.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.data.model.shared.Order

@Immutable
internal data class HomeState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)

@Immutable
internal sealed interface HomeEvent {
    data object LoadOrders : HomeEvent

    data object ToggleMenu : HomeEvent
}

@Immutable
internal sealed interface HomeSideEffect {
    data object ToggleMenu : HomeSideEffect

    data class ShowError(val message: String) : HomeSideEffect
}
