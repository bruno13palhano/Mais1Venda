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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.components.CircularProgress
import com.bruno13palhano.mais1venda.ui.screens.components.OrderListItem
import com.bruno13palhano.mais1venda.ui.screens.components.testCustomer
import com.bruno13palhano.mais1venda.ui.screens.components.testOrder
import com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel.NewOrdersViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle
import com.bruno13palhano.mais1venda.ui.theme.Mais1VendaTheme
import kotlinx.coroutines.launch

@Composable
internal fun NewOrdersRoute(
    navigateToNewOrder: (id: Long) -> Unit,
    navigateBack: () -> Unit,
    viewModel: NewOrdersViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { viewModel.handleEvent(event = NewOrdersEvent.LoadNewOrders) }

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                is NewOrdersSideEffect.ShowResponseError -> {
                    effect.remoteError?.let { error ->
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = error.description ?: "",
                                withDismissAction = true
                            )
                        }
                    }
                }

                is NewOrdersSideEffect.ShowError -> {
                    effect.errorType?.let { error ->
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = error.name,
                                withDismissAction = true
                            )
                        }
                    }
                }

                is NewOrdersSideEffect.NavigateToNewOrder -> navigateToNewOrder(effect.id)

                NewOrdersSideEffect.NavigateBack -> navigateBack()
            }
        }
    }

    NewOrdersContent(
        snackbarHostState = snackbarHostState,
        state = state,
        onEvent = viewModel::handleEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewOrdersContent(
    snackbarHostState: SnackbarHostState,
    state: NewOrdersState,
    onEvent: (event: NewOrdersEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.new_orders)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(NewOrdersEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back),
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) {
        if (state.loading) {
           CircularProgress(modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .consumeWindowInsets(it),
                contentPadding = PaddingValues(4.dp),
            ) {
                items(items = state.newOrders, key = { order -> order.id }) { order ->
                    OrderListItem(
                        customerName = order.customer.name,
                        productName = order.product.name,
                        orderDate = order.orderDate,
                        price = order.product.price,
                        onClick = { onEvent(NewOrdersEvent.NavigateToNewOrder(id = order.id)) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NewOrdersPreview() {
    Mais1VendaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            NewOrdersContent(
                snackbarHostState = remember { SnackbarHostState() },
                state = NewOrdersState(
                    newOrders = listOf(
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NewOrdersLoadingPreview() {
    Mais1VendaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            NewOrdersContent(
                snackbarHostState = remember { SnackbarHostState() },
                state = NewOrdersState(loading = true),
                onEvent = {},
            )
        }
    }
}
