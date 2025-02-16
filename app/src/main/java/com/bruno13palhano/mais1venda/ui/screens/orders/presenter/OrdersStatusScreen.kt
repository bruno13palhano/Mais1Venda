package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel.OrdersStatusViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle
import com.bruno13palhano.mais1venda.ui.theme.Mais1VendaTheme

@Composable
internal fun OrdersStatusRoute(
    navigateToNewOrders: () -> Unit,
    navigateToOrders: () -> Unit,
    navigateToCustomers: () -> Unit,
    navigateToDashboard: () -> Unit,
    navigateBack: () -> Unit,
    viewModel: OrdersStatusViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                OrdersStatusSideEffect.NavigateToNewOrders -> navigateToNewOrders()

                OrdersStatusSideEffect.NavigateToOrders -> navigateToOrders()

                OrdersStatusSideEffect.NavigateToCustomers -> navigateToCustomers()

                OrdersStatusSideEffect.NavigateToDashboard -> navigateToDashboard()

                OrdersStatusSideEffect.NavigateBack -> navigateBack()
            }
        }
    }

    OrdersStatusContent(state = state, onEvent = viewModel::handleEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrdersStatusContent(
    state: OrdersStatusState,
    onEvent: (event: OrdersStatusEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.orders_status)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(OrdersStatusEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back),
                        )
                    }
                },
            )
        },
    ) {
        val items = mapOf(
            stringResource(
                id = R.string.new_orders,
            ) to { onEvent(OrdersStatusEvent.NavigateToNewOrders) },
            stringResource(
                id = R.string.orders,
            ) to { onEvent(OrdersStatusEvent.NavigateToOrders) },
            stringResource(
                id = R.string.customers,
            ) to { onEvent(OrdersStatusEvent.NavigateToCustomers) },
            stringResource(
                id = R.string.dashboard,
            ) to { onEvent(OrdersStatusEvent.NavigateToDashboard) },
        )

        LazyVerticalGrid(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
        ) {
            items.forEach { (title, onClick) ->
                item {
                    CardItem(title = title, onClick = onClick)
                }
            }
        }
    }
}

@Composable
private fun CardItem(title: String, onClick: () -> Unit) {
    Card(onClick = onClick) {
        Card {
            Box(
                modifier = Modifier
                    .sizeIn(minHeight = 300.dp)
                    .fillMaxSize(),
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = title,
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun OrdersStatusPreview() {
    Mais1VendaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            OrdersStatusContent(
                state = OrdersStatusState(),
                onEvent = {},
            )
        }
    }
}
