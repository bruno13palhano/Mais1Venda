package com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.viewmodel.CreateAccountViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle

@Composable
internal fun CreateAccountRoute(viewModel: CreateAccountViewModel = hiltViewModel()) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    LaunchedEffect(effect) {
        effect.collect { sideEffect ->
            when (sideEffect) {
                CreateAccountSideEffect.NavigateToHome -> TODO()
                is CreateAccountSideEffect.ShowError -> TODO()
            }
        }
    }

    CreateAccountContent(
        state = state,
        onEvent = viewModel::handleEvent,
    )
}

@Composable
private fun CreateAccountContent(
    state: CreateAccountState,
    onEvent: (event: CreateAccountEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(it)
                    .consumeWindowInsets(it)
                    .verticalScroll(rememberScrollState()),
        ) {
        }
    }
}
