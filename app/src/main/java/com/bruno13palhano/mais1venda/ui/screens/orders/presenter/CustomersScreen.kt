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
import com.bruno13palhano.data.model.customer.Customer
import com.bruno13palhano.data.model.shared.Address
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.orders.viewmodel.CustomersViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle
import com.bruno13palhano.mais1venda.ui.theme.Mais1VendaTheme

@Composable
internal fun CustomersRoute(
    navigateBack: () -> Unit,
    viewModel: CustomersViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                CustomersSideEffect.NavigateBack -> navigateBack()
            }
        }
    }

    CustomersContent(state = state, onEvent = viewModel::handleEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomersContent(state: CustomersState, onEvent: (event: CustomersEvent) -> Unit) {
    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.customers)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(CustomersEvent.NavigateBack) }) {
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
            items(items = state.customers, key = { customer -> customer.uid }) {
                ListItem(
                    modifier = Modifier.padding(4.dp),
                    headlineContent = { Text(text = it.name) },
                    supportingContent = { Text(text = it.email) },
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CustomersPreview() {
    Mais1VendaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            CustomersContent(
                state = CustomersState(
                    customers = listOf(
                        customer,
                        customer.copy(uid = "2", name = "Customer 2"),
                        customer.copy(uid = "3", name = "Customer 3"),
                        customer.copy(uid = "4", name = "Customer 4"),
                        customer.copy(uid = "5", name = "Customer 5"),
                        customer.copy(uid = "6", name = "Customer 6"),
                        customer.copy(uid = "7", name = "Customer 7"),
                        customer.copy(uid = "8", name = "Customer 8"),
                    )
                ),
                onEvent = {},
            )
        }
    }
}

private val customer = Customer(
    uid = "1",
    name = "Customer 1",
    email = "email 1",
    phone = "phone 1",
    address = Address("", "", "", ""),
    socialMedia = emptyList(),
    orders = emptyList(),
    lastModifiedTimestamp = ""
)
