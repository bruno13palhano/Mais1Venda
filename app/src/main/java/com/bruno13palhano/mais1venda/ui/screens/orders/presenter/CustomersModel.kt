package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.data.model.customer.Customer

@Immutable
internal data class CustomersState(
    val customers: List<Customer> = emptyList(),
    val selectedCustomer: Customer? = null,
    val showCustomerInfo: Boolean = false,
)

@Immutable
internal sealed interface CustomersEvent {
    data object LoadCustomers : CustomersEvent
    data class CustomerInfo(val customer: Customer) : CustomersEvent
    data object ToggleShowCustomerInfo : CustomersEvent
    data object NavigateBack : CustomersEvent
}

@Immutable
internal sealed interface CustomersSideEffect {
    data object NavigateBack : CustomersSideEffect
}
