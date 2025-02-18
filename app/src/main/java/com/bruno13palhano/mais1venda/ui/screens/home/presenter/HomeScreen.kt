package com.bruno13palhano.mais1venda.ui.screens.home.presenter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.home.viewmodel.HomeViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle

@Composable
internal fun HomeRoute(
    openDrawer: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToOrdersStatus: () -> Unit,
    navigateToProducts: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state = viewModel.container.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    if (!state.value.authenticated) viewModel.handleEvent(event = HomeEvent.NavigateToLogin)

    LaunchedEffect(effect) {
        effect.collect { sideEffect ->
            when (sideEffect) {
                HomeSideEffect.ToggleMenu -> openDrawer()

                is HomeSideEffect.ShowError -> {}

                HomeSideEffect.NavigateToLogin -> navigateToLogin()

                HomeSideEffect.NavigateToOrdersStatus -> navigateToOrdersStatus()

                HomeSideEffect.NavigateToProducts -> navigateToProducts()
            }
        }
    }

    HomeContent(state = state.value, onEvent = viewModel::handleEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(state: HomeState, onEvent: (HomeEvent) -> Unit) {
    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(HomeEvent.ToggleMenu) }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(R.string.burger_menu),
                        )
                    }
                },
            )
        },
    ) {
        val items = mapOf(
            stringResource(
                id = R.string.products,
            ) to { onEvent(HomeEvent.NavigateToProducts) },
            stringResource(
                id = R.string.orders_status,
            ) to { onEvent(HomeEvent.NavigateToOrdersStatus) },
        )

        Column(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it)
                .verticalScroll(rememberScrollState()),
        ) {
            items.forEach { (title, function) ->
                CardItem(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    title = title,
                    onClick = function
                )
            }
        }
    }
}

@Composable
private fun CardItem(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .sizeIn(minHeight = 300.dp)
                .fillMaxSize(),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = title,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeContentPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            HomeContent(
                state = HomeState(authenticated = true),
                onEvent = {},
            )
        }
    }
}
