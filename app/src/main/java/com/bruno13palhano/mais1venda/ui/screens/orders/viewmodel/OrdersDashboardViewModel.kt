package com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersDashboardEvent
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersDashboardSideEffect
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersDashboardState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class OrdersDashboardViewModel @Inject constructor(
    initialStates: OrdersDashboardState,
) : ViewModel() {
    val container = Container<OrdersDashboardState, OrdersDashboardSideEffect>(
        initialState = initialStates,
        scope = viewModelScope,
    )

    fun handleEvent(event: OrdersDashboardEvent) {
        when (event) {
            OrdersDashboardEvent.NavigateBack -> container.intent {
                postSideEffect(effect = OrdersDashboardSideEffect.NavigateBack)
            }
        }
    }
}
