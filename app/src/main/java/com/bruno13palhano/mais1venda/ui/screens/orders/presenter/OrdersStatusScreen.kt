package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
    navigateBack: () -> Unit,
    viewModel: OrdersStatusViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    LaunchedEffect(Unit) {
        sideEffect.collect { effect ->
            when (effect) {
                OrdersStatusSideEffect.NavigateToNewOrders -> navigateToNewOrders()

                OrdersStatusSideEffect.NavigateToOrders -> navigateToOrders()

                OrdersStatusSideEffect.NavigateToCustomers -> navigateToCustomers()

                OrdersStatusSideEffect.NavigateBack -> navigateBack()
            }
        }
    }

    OrdersStatusContent(
        state = state,
        onEvent = viewModel::handleEvent,
    )
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
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
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
