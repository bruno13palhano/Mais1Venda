package com.bruno13palhano.mais1venda.ui.screens.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.mais1venda.ui.screens.home.presenter.HomeEvent
import com.bruno13palhano.mais1venda.ui.screens.home.presenter.HomeSideEffect
import com.bruno13palhano.mais1venda.ui.screens.home.presenter.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    initialState: HomeState,
) : ViewModel() {
    val container = Container<HomeState, HomeSideEffect>(
        initialState = initialState,
        scope = viewModelScope,
    )

    fun handleEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.LoadOrders -> loadOrders()

            HomeEvent.ToggleMenu -> toggleMenu()

            HomeEvent.NavigateToProducts -> navigateToProducts()
        }
    }

    private fun loadOrders() = container.intent {
        reduce { copy(isLoading = true) }
        delay(3000)
        reduce { copy(isLoading = false) }
    }

    private fun toggleMenu() = container.intent {
        postSideEffect(HomeSideEffect.ToggleMenu)
    }

    private fun navigateToProducts() = container.intent {
        postSideEffect(effect = HomeSideEffect.NavigateToProducts)
    }
}
