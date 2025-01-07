package com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.viewmodel.LoginViewModel
import com.bruno13palhano.mais1venda.ui.screens.components.CircularProgress
import com.bruno13palhano.mais1venda.ui.screens.components.CustomPasswordTextField
import com.bruno13palhano.mais1venda.ui.screens.components.CustomTextField
import com.bruno13palhano.mais1venda.ui.screens.shared.clickableWithoutRipple
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle
import kotlinx.coroutines.launch

@Composable
internal fun LoginRoute(
    navigateToHome: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    navigateToCreateAccount: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state = viewModel.container.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(effect) {
        effect.collect { sideEffect ->
            when (sideEffect) {
                LoginSideEffect.NavigateToHome -> navigateToHome()

                LoginSideEffect.NavigateToForgotPassword -> navigateToForgotPassword()

                LoginSideEffect.NavigateToCreateAccount -> navigateToCreateAccount()

                LoginSideEffect.DismissKeyboard -> {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }

                is LoginSideEffect.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = sideEffect.message,
                            withDismissAction = true,
                        )
                    }
                }
            }
        }
    }

    LoginContent(
        state = state.value,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::handleEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginContent(
    state: LoginState,
    snackbarHostState: SnackbarHostState,
    onEvent: (LoginEvent) -> Unit,
) {
    Scaffold(
        modifier =
            Modifier
                .clickableWithoutRipple { onEvent(LoginEvent.DismissKeyboard) }
                .consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.login)) },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(LoginEvent.Login) }) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = "Done")
            }
        },
    ) {
        if (state.isLoading) {
            CircularProgress(
                modifier =
                    Modifier
                        .padding(it)
                        .fillMaxSize(),
            )
        } else {
            // workaround needed to TextField work properly
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            Column(
                modifier =
                    Modifier
                        .padding(it)
                        .consumeWindowInsets(it)
                        .verticalScroll(rememberScrollState()),
            ) {
                CustomTextField(
                    Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    value = email,
                    onValueChange = { newEmail ->
                        email = newEmail
                        onEvent(LoginEvent.EmailChanged(email = newEmail))
                    },
                    label = stringResource(R.string.email),
                    placeholder = stringResource(R.string.enter_email),
                )

                CustomPasswordTextField(
                    Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    visibility = state.passwordVisibility,
                    value = password,
                    onValueChange = { newPassword ->
                        password = newPassword
                        onEvent(LoginEvent.PasswordChanged(password = newPassword))
                    },
                    togglePasswordVisibility = { onEvent(LoginEvent.TogglePasswordVisibility) },
                    label = stringResource(R.string.password),
                    placeholder = stringResource(R.string.enter_password),
                )
            }
        }
    }
}
