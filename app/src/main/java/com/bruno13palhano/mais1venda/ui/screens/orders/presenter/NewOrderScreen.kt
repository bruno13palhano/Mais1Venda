package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel.NewOrderViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.clickableWithoutRipple
import com.bruno13palhano.mais1venda.ui.screens.shared.dateFormat
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle
import com.bruno13palhano.mais1venda.ui.theme.Mais1VendaTheme

@Composable
internal fun NewOrderRoute(
    id: Long,
    navigateBack: () -> Unit,
    viewModel: NewOrderViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    LaunchedEffect(Unit) { viewModel.handleEvent(event = NewOrderEvent.LoadOrder(id = id)) }

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                is NewOrderSideEffect.ShowError -> {}

                NewOrderSideEffect.DismissKeyboard -> {}

                NewOrderSideEffect.NavigateBack -> navigateBack()
            }
        }
    }

    NewOrderContent(state = state, onEvent = viewModel::handleEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewOrderContent(state: NewOrderState, onEvent: (event: NewOrderEvent) -> Unit) {
    Scaffold(
        modifier = Modifier
            .clickableWithoutRipple { onEvent(NewOrderEvent.DismissKeyboard) }
            .consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.new_order)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(NewOrderEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back),
                        )
                    }
                },
            )
        },
    ) {
        NewOrderForm(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            state = state,
            onEvent = onEvent,
        )
    }
}

@Composable
private fun NewOrderForm(
    modifier: Modifier = Modifier,
    state: NewOrderState,
    onEvent: (event: NewOrderEvent) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(R.string.product_info),
            style = MaterialTheme.typography.titleMedium,
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        ListItem(
            headlineContent = { Text(text = state.order?.product?.name ?: "") },
            overlineContent = { Text(text = stringResource(id = R.string.product_name)) },
        )
        ListItem(
            headlineContent = { Text(text = state.order?.product?.description ?: "") },
            overlineContent = { Text(text = stringResource(id = R.string.description)) },
        )
        ListItem(
            headlineContent = { Text(text = state.order?.product?.quantity.toString()) },
            overlineContent = { Text(text = stringResource(id = R.string.quantity)) },
        )
        ListItem(
            headlineContent = { Text(text = state.order?.product?.name ?: "") },
            overlineContent = { Text(text = stringResource(id = R.string.price)) },
        )
        ListItem(
            headlineContent = { Text(text = state.order?.product?.category?.first() ?: "") },
            overlineContent = { Text(text = stringResource(id = R.string.category)) },
        )
        ListItem(
            headlineContent = { Text(text = state.order?.product?.code ?: "") },
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
            headlineContent = { Text(text = state.order?.customer?.name ?: "") },
            overlineContent = { Text(text = stringResource(id = R.string.customer_name)) },
        )
        ListItem(
            headlineContent = { Text(text = state.order?.customer?.email ?: "") },
            overlineContent = { Text(text = stringResource(id = R.string.email)) },
        )
        ListItem(
            headlineContent = { Text(text = state.order?.customer?.phone ?: "") },
            overlineContent = { Text(text = stringResource(id = R.string.phone)) },
        )
        ListItem(
            headlineContent = { Text(text = state.order?.customer?.address?.street ?: "") },
            overlineContent = { Text(text = stringResource(id = R.string.street)) },
        )
        ListItem(
            headlineContent = { Text(text = state.order?.customer?.address?.number ?: "") },
            overlineContent = { Text(text = stringResource(id = R.string.number)) },
        )
        ListItem(
            headlineContent = { Text(text = state.order?.customer?.address?.complement ?: "") },
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
            headlineContent = { Text(text = dateFormat.format(state.order?.orderDate)) },
            overlineContent = { Text(text = stringResource(R.string.order_date)) },
        )
        ListItem(
            headlineContent = { Text(text = dateFormat.format(state.order?.deliveryDate)) },
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

        Button(
            modifier = Modifier
                .padding(start = 8.dp, top = 32.dp, end = 8.dp)
                .fillMaxWidth(),
            onClick = { onEvent(NewOrderEvent.ConfirmOrder) }
        ) {
            Text(text = stringResource(R.string.confirm_order))
        }

        Row(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            TextButton(onClick = { onEvent(NewOrderEvent.CancelOrder) }) {
                Text(text = stringResource(R.string.cancel_order))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NewOrderPreview() {
    Mais1VendaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            NewOrderContent(
                state = NewOrderState(order = order),
                onEvent = {},
            )
        }
    }
}

private val product = Product(
    id = 1,
    name = "Product 1",
    price = 1.0f,
    category = listOf("Category 1"),
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
