package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.components.MoreVertMenu
import com.bruno13palhano.mais1venda.ui.screens.components.OrderListItem
import com.bruno13palhano.mais1venda.ui.screens.components.testCustomer
import com.bruno13palhano.mais1venda.ui.screens.components.testOrder
import com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel.OrdersViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle
import com.bruno13palhano.mais1venda.ui.theme.Mais1VendaTheme

@Composable
internal fun OrdersRoute(navigateBack: () -> Unit, viewModel: OrdersViewModel = hiltViewModel()) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    LaunchedEffect(Unit) { viewModel.handleEvent(event = OrdersEvent.LoadOrders) }

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                OrdersSideEffect.NavigateBack -> navigateBack()
            }
        }
    }

    OrdersContent(state = state, onEvent = viewModel::handleEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrdersContent(state: OrdersState, onEvent: (event: OrdersEvent) -> Unit) {
    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.orders)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(OrdersEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back),
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { onEvent(OrdersEvent.ToggleOptionMenu) }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(R.string.more_options_menu),
                            )
                        }

                        val items = getMoreOptionsItems()

                        MoreVertMenu(
                            items = items,
                            expanded = state.openOptionMenu,
                            onDismissRequest = { onEvent(OrdersEvent.ToggleOptionMenu) },
                            onItemClick = { onEvent(OrdersEvent.UpdateSelectedOption(option = it)) }
                        )
                    }
                }
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it),
            contentPadding = PaddingValues(4.dp),
        ) {
            items(items = state.orders, key = { order -> order.id }) { order ->
                OrderListItem(
                    customerName = order.customer.name,
                    productName = order.product.name,
                    orderDate = order.orderDate,
                    price = order.product.price,
                    onClick = { onEvent(OrdersEvent.OrderInfo(order = order)) }
                )
            }
        }

        if (state.showOrderInfo) {
            state.selectedOrder?.let { order ->

            }
        }
    }
}

@Composable
private fun getMoreOptionsItems(): Map<String, OrdersMenuItems> {
    return mapOf(
        stringResource(id = R.string.product_name) to OrdersMenuItems.SORT_BY_PRODUCT_NAME,
        stringResource(id = R.string.customer_name) to OrdersMenuItems.SORT_BY_CUSTOMER_NAME,
        stringResource(id = R.string.price) to OrdersMenuItems.SORT_BY_PRICE,
        stringResource(id = R.string.quantity) to OrdersMenuItems.SORT_BY_QUANTITY,
        stringResource(id = R.string.order_date) to OrdersMenuItems.SORT_BY_ORDER_DATE,
        stringResource(id = R.string.delivery_date) to OrdersMenuItems.SORT_BY_DELIVERY_DATE,
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OrdersPreview() {
    Mais1VendaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            OrdersContent(
                state = OrdersState(
                    orders = listOf(
                        testOrder,
                        testOrder.copy(id = 2, customer = testCustomer.copy(name = "Customer 2")),
                        testOrder.copy(id = 3, customer = testCustomer.copy(name = "Customer 3")),
                        testOrder.copy(id = 4, customer = testCustomer.copy(name = "Customer 4")),
                        testOrder.copy(id = 5, customer = testCustomer.copy(name = "Customer 5")),
                        testOrder.copy(id = 6, customer = testCustomer.copy(name = "Customer 6")),
                        testOrder.copy(id = 7, customer = testCustomer.copy(name = "Customer 7")),
                        testOrder.copy(id = 8, customer = testCustomer.copy(name = "Customer 8")),
                        testOrder.copy(id = 9, customer = testCustomer.copy(name = "Customer 9")),
                        testOrder.copy(id = 10, customer = testCustomer.copy(name = "Customer 10")),
                    ),
                ),
                onEvent = {},
            )
        }
    }
}

