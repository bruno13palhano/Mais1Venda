package com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.data.repository.CustomerRepository
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.CustomersEvent
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.CustomersSideEffect
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.CustomersState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class CustomersViewModel @Inject constructor(
    initialState: CustomersState,
    private val customerRepository: CustomerRepository,
) : ViewModel() {
    val container = Container<CustomersState, CustomersSideEffect>(
        initialState = initialState,
        scope = viewModelScope,
    )

    fun handleEvent(event: CustomersEvent) {
        when (event) {
            CustomersEvent.LoadCustomers -> loadCustomers()

            CustomersEvent.NavigateBack -> navigateBack()
        }
    }

    private fun loadCustomers() = container.intent {
        customerRepository.getAll().collect { customers ->
            reduce { copy(customers = customers) }
        }
    }

    private fun navigateBack() = container.intent {
        postSideEffect(effect = CustomersSideEffect.NavigateBack)
    }
}
