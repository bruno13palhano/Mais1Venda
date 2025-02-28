package com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.model.shared.Order
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.data.repository.OrderRepository
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersEvent
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersMenuItems
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersSideEffect
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class OrdersViewModel @Inject constructor(
    initialStates: OrdersState,
    private val orderRepository: OrderRepository,
) : ViewModel() {
    val container = Container<OrdersState, OrdersSideEffect>(
        initialState = initialStates,
        scope = viewModelScope,
    )

    fun handleEvent(event: OrdersEvent) {
        when (event) {
            OrdersEvent.LoadOrders -> loadOrders()

            is OrdersEvent.OrderInfo -> orderInfo(order = event.order)

            OrdersEvent.ToggleShowOrderInfo -> toggleShowOrderInfo()

            OrdersEvent.ToggleOptionMenu -> toggleOptionMenu()

            is OrdersEvent.UpdateSelectedOption -> updateSelectedOption(option = event.option)

            OrdersEvent.NavigateBack -> navigateBack()
        }
    }

    private fun loadOrders() = container.intent {
        orderRepository.getAll().collect {
            reduce { copy(orders = it) }
        }
    }

    private fun orderInfo(order: Order) = container.intent {
        reduce { copy(selectedOrder = order, showOrderInfo = !showOrderInfo) }
    }

    private fun toggleShowOrderInfo() = container.intent {
        reduce { copy(showOrderInfo = !showOrderInfo) }
    }

    private fun toggleOptionMenu() = container.intent {
        reduce { copy(openOptionMenu = !openOptionMenu) }
    }

    private fun updateSelectedOption(option: OrdersMenuItems) = container.intent {
        when (option) {
            OrdersMenuItems.SORT_BY_CUSTOMER_NAME -> {
                reduce {
                    copy(
                        orders = orders.sortedBy { it.customer.name },
                        openOptionMenu = false,
                    )
                }
            }

            OrdersMenuItems.SORT_BY_PRODUCT_NAME -> {
                reduce {
                    copy(
                        orders = orders.sortedBy { it.productName },
                        openOptionMenu = false,
                    )
                }
            }

            OrdersMenuItems.SORT_BY_PRICE -> {
            }

            OrdersMenuItems.SORT_BY_QUANTITY -> {
                reduce {
                    copy(
                        orders = orders.sortedBy { it.quantity },
                        openOptionMenu = false,
                    )
                }
            }

            OrdersMenuItems.SORT_BY_ORDER_DATE -> {
                reduce {
                    copy(
                        orders = orders.sortedBy { it.orderDate },
                        openOptionMenu = false,
                    )
                }
            }

            OrdersMenuItems.SORT_BY_DELIVERY_DATE -> {
                reduce {
                    copy(
                        orders = orders.sortedBy { it.deliveryDate },
                        openOptionMenu = false,
                    )
                }
            }
        }
    }

    private fun navigateBack() = container.intent {
        postSideEffect(effect = OrdersSideEffect.NavigateBack)
    }
}
