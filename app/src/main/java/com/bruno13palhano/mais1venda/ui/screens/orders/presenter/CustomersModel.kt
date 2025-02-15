package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.data.model.customer.Customer

@Immutable
internal data class CustomersState(
    val customers: List<Customer> = emptyList(),
)

@Immutable
internal sealed interface CustomersEvent {
    data object LoadCustomers : CustomersEvent
    data object NavigateBack : CustomersEvent
}

@Immutable
internal sealed interface CustomersSideEffect {
    data object NavigateBack : CustomersSideEffect
}
