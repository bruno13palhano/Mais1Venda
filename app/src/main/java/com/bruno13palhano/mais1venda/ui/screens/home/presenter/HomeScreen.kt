package com.bruno13palhano.mais1venda.ui.screens.home.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.home.viewmodel.HomeViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle
import kotlin.random.Random

@Composable
internal fun HomeRoute(
    openDrawer: () -> Unit,
    navigateToLogin: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.container.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    LaunchedEffect(Unit) {
        if (Random.nextBoolean()) {
            navigateToLogin()
        }
    }

    LaunchedEffect(effect) {
        effect.collect { sideEffect ->
            when (sideEffect) {
                HomeSideEffect.ToggleMenu -> openDrawer()

                is HomeSideEffect.ShowError -> {}
            }
        }
    }

    HomeContent(
        state = state.value,
        onEvent = viewModel::handleEvent,
    )
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
                }
            )
        },
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(it)
                    .consumeWindowInsets(it)
                    .verticalScroll(rememberScrollState()),
            ) {
            }
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
                state = HomeState(isLoading = true),
                onEvent = {},
            )
        }
    }
}
