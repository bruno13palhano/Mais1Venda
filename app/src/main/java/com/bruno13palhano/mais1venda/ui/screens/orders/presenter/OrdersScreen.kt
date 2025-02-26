package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.data.model.shared.Order
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.components.MoreVertMenu
import com.bruno13palhano.mais1venda.ui.screens.components.OrderListItem
import com.bruno13palhano.mais1venda.ui.screens.components.testCustomer
import com.bruno13palhano.mais1venda.ui.screens.components.testOrder
import com.bruno13palhano.mais1venda.ui.screens.components.testProduct
import com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel.OrdersViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.dateFormat
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
                            onItemClick = {
                                onEvent(
                                    OrdersEvent.UpdateSelectedOption(option = it),
                                )
                            },
                        )
                    }
                },
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
                    price = 0.0f,
                    onClick = { onEvent(OrdersEvent.OrderInfo(order = order)) },
                )
            }
        }

        if (state.showOrderInfo) {
            state.selectedOrder?.let { order ->
                SelectedOrder(
                    modifier = Modifier
                        .padding(it)
                        .consumeWindowInsets(it)
                        .padding(32.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    order = order,
                    onClick = { onEvent(OrdersEvent.ToggleShowOrderInfo) },
                )
            }
        }
    }
}

@Composable
private fun SelectedOrder(modifier: Modifier = Modifier, order: Order, onClick: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.background.copy(alpha = .7f)
    ) {
        OutlinedCard(modifier = modifier) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 12.dp, end = 8.dp),
                    text = stringResource(R.string.product_info),
                    style = MaterialTheme.typography.titleMedium,
                )

                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = onClick
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            ListItem(
                headlineContent = { Text(text = order.product.name) },
                overlineContent = { Text(text = stringResource(id = R.string.product_name)) },
            )
            ListItem(
                headlineContent = { Text(text = order.product.description) },
                overlineContent = { Text(text = stringResource(id = R.string.description)) },
            )
            ListItem(
                headlineContent = { Text(text = order.product.quantity.toString()) },
                overlineContent = { Text(text = stringResource(id = R.string.quantity)) },
            )
            ListItem(
                headlineContent = { Text(text = order.product.name) },
                overlineContent = { Text(text = stringResource(id = R.string.price)) },
            )
            ListItem(
                headlineContent = { Text(text = order.product.category.first()) },
                overlineContent = { Text(text = stringResource(id = R.string.category)) },
            )
            ListItem(
                headlineContent = { Text(text = order.product.code) },
                overlineContent = { Text(text = stringResource(id = R.string.code)) },
            )

            HorizontalDivider(modifier = Modifier.padding(top = 24.dp, bottom = 8.dp))

            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(R.string.customer_info),
                style = MaterialTheme.typography.titleMedium,
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            ListItem(
                headlineContent = { Text(text = order.customer.name) },
                overlineContent = { Text(text = stringResource(id = R.string.customer_name)) },
            )
            ListItem(
                headlineContent = { Text(text = order.customer.email) },
                overlineContent = { Text(text = stringResource(id = R.string.email)) },
            )
            ListItem(
                headlineContent = { Text(text = order.customer.phone) },
                overlineContent = { Text(text = stringResource(id = R.string.phone)) },
            )
            ListItem(
                headlineContent = { Text(text = order.customer.address.street) },
                overlineContent = { Text(text = stringResource(id = R.string.street)) },
            )
            ListItem(
                headlineContent = { Text(text = order.customer.address.number) },
                overlineContent = { Text(text = stringResource(id = R.string.number)) },
            )
            ListItem(
                headlineContent = { Text(text = order.customer.address.complement) },
                overlineContent = { Text(text = stringResource(id = R.string.complement)) },
            )

            HorizontalDivider(modifier = Modifier.padding(top = 24.dp, bottom = 8.dp))

            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(R.string.delivery_info),
                style = MaterialTheme.typography.titleMedium,
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            ListItem(
                headlineContent = { Text(text = dateFormat.format(order.orderDate)) },
                overlineContent = { Text(text = stringResource(R.string.order_date)) },
            )
            ListItem(
                headlineContent = { Text(text = dateFormat.format(order.deliveryDate)) },
                overlineContent = { Text(text = stringResource(R.string.delivery_date)) },
            )

            HorizontalDivider(modifier = Modifier.padding(top = 24.dp, bottom = 8.dp))

            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(R.string.status_info),
                style = MaterialTheme.typography.titleMedium,
            )
            ListItem(
                headlineContent = { Text(text = "Current status") },
                overlineContent = { Text(text = stringResource(R.string.status)) },
            )
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
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OrdersDynamicPreview() {
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

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OrdersPreview() {
    Mais1VendaTheme(dynamicColor = false) {
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
                    selectedOrder = testOrder.copy(
                        id = 2,
                        product = testProduct.copy(category = listOf("test")),
                        customer = testCustomer.copy(name = "Customer 2"),
                    ),
                    showOrderInfo = true,
                ),
                onEvent = {},
            )
        }
    }
}
