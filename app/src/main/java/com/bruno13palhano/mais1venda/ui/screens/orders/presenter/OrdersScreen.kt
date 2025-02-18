package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.customer.Customer
import com.bruno13palhano.data.model.shared.Address
import com.bruno13palhano.data.model.shared.Order
import com.bruno13palhano.data.model.shared.OrderStatus
import com.bruno13palhano.mais1venda.R
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
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it),
            contentPadding = PaddingValues(4.dp),
        ) {
            items(items = state.orders, key = { order -> order.id }) {
                ListItem(
                    modifier = Modifier.padding(4.dp),
                    overlineContent = { Text(text = it.customer.name) },
                    headlineContent = { Text(text = it.product.name) },
                    supportingContent = { Text(text = dateFormat.format(it.orderDate)) },
                )
            }
        }
    }
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
                        order,
                        order.copy(id = 2, customer = customer.copy(name = "Customer 2")),
                        order.copy(id = 3, customer = customer.copy(name = "Customer 3")),
                        order.copy(id = 4, customer = customer.copy(name = "Customer 4")),
                        order.copy(id = 5, customer = customer.copy(name = "Customer 5")),
                        order.copy(id = 6, customer = customer.copy(name = "Customer 6")),
                        order.copy(id = 7, customer = customer.copy(name = "Customer 7")),
                        order.copy(id = 8, customer = customer.copy(name = "Customer 8")),
                    ),
                ),
                onEvent = {},
            )
        }
    }
}

private val product = Product(
    id = 1,
    name = "Product 1",
    price = 1.0f,
    category = emptyList(),
    description = "Description 1",
    code = "1",
    quantity = 1,
    exhibitToCatalog = false,
    lastModifiedTimestamp = "",
)

private val customer = Customer(
    uid = "1",
    name = "Customer 1",
    email = "email 1",
    phone = "phone 1",
    address = Address("", "", "", ""),
    socialMedia = emptyList(),
    orders = emptyList(),
    lastModifiedTimestamp = "",
)

private val order = Order(
    id = 1,
    product = product,
    customer = customer,
    orderDate = 111111111111212,
    deliveryDate = 12234523453245,
    status = OrderStatus.PROCESSING_ORDER,
    lastModifiedTimestamp = "",
)
