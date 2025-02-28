package com.bruno13palhano.mais1venda.ui.screens.home.presenter

import androidx.compose.runtime.Immutable

@Immutable
internal data class HomeState(
    val authenticated: Boolean = false,
)

@Immutable
internal sealed interface HomeEvent {
    data object ToggleMenu : HomeEvent
    data object NavigateToLogin : HomeEvent
    data object NavigateToOrdersStatus : HomeEvent
    data object NavigateToProducts : HomeEvent
    data object NavigateToAds : HomeEvent
}

@Immutable
internal sealed interface HomeSideEffect {
    data object ToggleMenu : HomeSideEffect
    data class ShowError(val message: String) : HomeSideEffect
    data object NavigateToLogin : HomeSideEffect
    data object NavigateToOrdersStatus : HomeSideEffect
    data object NavigateToProducts : HomeSideEffect
    data object NavigateToAds : HomeSideEffect
}
