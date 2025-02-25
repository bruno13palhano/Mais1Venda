package com.bruno13palhano.mais1venda.ui.screens.ads.presenter

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.ads.viewmodel.AdViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle
import com.bruno13palhano.mais1venda.ui.theme.Mais1VendaTheme
import kotlinx.coroutines.launch

@Composable
internal fun AdNewRoute(
    navigateBack: () -> Unit,
    viewModel: AdViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val inputFieldsErrorMessage = stringResource(id = R.string.invalid_fields)

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                is AdSideEffect.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = inputFieldsErrorMessage,
                            withDismissAction = true,
                        )
                    }
                }

                AdSideEffect.DismissKeyboard -> {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }

                AdSideEffect.NavigateBack -> navigateBack()
            }
        }
    }

    AdContent(
        snackbarHostState = snackbarHostState,
        state = state,
        onEvent = viewModel::handleEvent
    )
}

@Composable
internal fun EditAdRoute(
    id: Long,
    navigateBack: () -> Unit,
    viewModel: AdViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val inputFieldsErrorMessage = stringResource(id = R.string.invalid_fields)

    LaunchedEffect(Unit) { viewModel.handleEvent(event = AdEvent.GetAd(id = id)) }

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                is AdSideEffect.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = inputFieldsErrorMessage,
                            withDismissAction = true,
                        )
                    }
                }

                AdSideEffect.DismissKeyboard -> {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }

                AdSideEffect.NavigateBack -> navigateBack()
            }
        }
    }

    AdContent(
        snackbarHostState = snackbarHostState,
        state = state,
        onEvent = viewModel::handleEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdContent(
    snackbarHostState: SnackbarHostState,
    state: AdState,
    onEvent: (event: AdEvent) -> Unit,
) {
    val title = if (state.id == null) stringResource(id = R.string.new_ad)
        else stringResource(id = R.string.edit_ad)

    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(AdEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back),
                        )
                    }
                }
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
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AdPreview() {
    Mais1VendaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AdContent(
                snackbarHostState = remember { SnackbarHostState() },
                state = AdState(),
                onEvent = {}
            )
        }
    }
}
