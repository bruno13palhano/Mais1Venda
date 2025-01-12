package com.bruno13palhano.mais1venda.ui.screens.home.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.mais1venda.ui.screens.home.viewmodel.HomeViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle
import kotlin.random.Random

@Composable
internal fun HomeRoute(navigateToLogin: () -> Unit, viewModel: HomeViewModel = hiltViewModel()) {
    val state = viewModel.container.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    LaunchedEffect(Unit) {
        if (Random.nextBoolean()) {
            println("navigate to login")
            navigateToLogin()
        }
    }

    LaunchedEffect(effect) {
        effect.collect { sideEffect ->
            when (sideEffect) {
                HomeSideEffect.ToggleMenu -> {}

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
                title = { Text(text = "Home") },
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
