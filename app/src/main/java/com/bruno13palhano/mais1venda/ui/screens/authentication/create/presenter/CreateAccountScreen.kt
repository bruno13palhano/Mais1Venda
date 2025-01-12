package com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.viewmodel.CreateAccountViewModel
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.getErrorMessages
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle
import kotlinx.coroutines.launch

@Composable
internal fun CreateAccountRoute(
    navigateToHome: () -> Unit,
    navigateBack: () -> Unit,
    viewModel: CreateAccountViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val errorMessages = getErrorMessages()

    LaunchedEffect(effect) {
        effect.collect { sideEffect ->
            when (sideEffect) {
                CreateAccountSideEffect.DismissKeyboard -> {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }

                CreateAccountSideEffect.NavigateToHome -> navigateToHome()

                CreateAccountSideEffect.NavigateBack -> navigateBack()

                is CreateAccountSideEffect.ShowError -> {
                    errorMessages[sideEffect.codeError]?.let { message ->
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = message,
                                withDismissAction = true,
                            )
                        }
                    }
                }
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
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it)
                .verticalScroll(rememberScrollState()),
        ) {
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateAccountContentPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            CreateAccountContent(
                state = CreateAccountState(),
                onEvent = {},
            )
        }
    }
}
