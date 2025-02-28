package com.bruno13palhano.mais1venda.ui.screens.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.mais1venda.ui.screens.home.presenter.HomeEvent
import com.bruno13palhano.mais1venda.ui.screens.home.presenter.HomeSideEffect
import com.bruno13palhano.mais1venda.ui.screens.home.presenter.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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
            HomeEvent.ToggleMenu -> toggleMenu()

            HomeEvent.NavigateToLogin -> navigateToLogin()

            HomeEvent.NavigateToOrdersStatus -> navigateToOrdersStatus()

            HomeEvent.NavigateToProducts -> navigateToProducts()

            HomeEvent.NavigateToAds -> navigateToAds()
        }
    }

    private fun navigateToLogin() = container.intent {
        postSideEffect(effect = HomeSideEffect.NavigateToLogin)
    }

    private fun navigateToOrdersStatus() = container.intent {
        postSideEffect(effect = HomeSideEffect.NavigateToOrdersStatus)
    }

    private fun toggleMenu() = container.intent {
        postSideEffect(effect = HomeSideEffect.ToggleMenu)
    }

    private fun navigateToProducts() = container.intent {
        postSideEffect(effect = HomeSideEffect.NavigateToProducts)
    }

    private fun navigateToAds() = container.intent {
        postSideEffect(effect = HomeSideEffect.NavigateToAds)
    }
}
