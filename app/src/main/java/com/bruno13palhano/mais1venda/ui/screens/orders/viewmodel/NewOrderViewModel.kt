package com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrderEvent
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrderSideEffect
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrderState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class NewOrderViewModel @Inject constructor(
    initialState: NewOrderState,
) : ViewModel() {
    val container = Container<NewOrderState, NewOrderSideEffect>(
        initialState = initialState,
        scope = viewModelScope,
    )

    fun handleEvent(event: NewOrderEvent) {
        when (event) {
            is NewOrderEvent.LoadOrder -> {}

            NewOrderEvent.NavigateBack -> {}
        }
    }
}
