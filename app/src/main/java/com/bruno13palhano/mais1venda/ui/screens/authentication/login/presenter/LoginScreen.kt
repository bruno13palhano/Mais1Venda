package com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
        modifier = Modifier
            .clickableWithoutRipple { onEvent(LoginEvent.DismissKeyboard) }
            .consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.login)) },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) {
        if (state.isLoading) {
            CircularProgress(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(it)
                    .consumeWindowInsets(it)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                CustomTextField(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    value = state.email,
                    onValueChange = { email -> onEvent(LoginEvent.EmailChanged(email = email)) },
                    label = stringResource(R.string.email),
                    placeholder = stringResource(R.string.enter_email),
                    isError = state.emailError,
                )

                CustomPasswordTextField(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    visibility = state.passwordVisibility,
                    value = state.password,
                    onValueChange = { password ->
                        onEvent(
                            LoginEvent.PasswordChanged(password = password),
                        )
                    },
                    togglePasswordVisibility = { onEvent(LoginEvent.TogglePasswordVisibility) },
                    label = stringResource(R.string.password),
                    placeholder = stringResource(R.string.enter_password),
                    isError = state.passwordError,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = { onEvent(LoginEvent.ForgotPassword) }) {
                        Text(
                            text = stringResource(id = R.string.forgot_your_password),
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

                Button(
                    modifier = Modifier
                        .padding(start = 24.dp, top = 32.dp, bottom = 8.dp, end = 24.dp)
                        .fillMaxWidth(),
                    onClick = { onEvent(LoginEvent.Login) },
                ) {
                    Text(text = stringResource(id = R.string.login))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(12.dp)
                            .weight(1f),
                    )
                    Text(
                        text = stringResource(R.string.or),
                        fontWeight = FontWeight.Medium,
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(12.dp)
                            .weight(1f),
                    )
                }

                Button(
                    modifier = Modifier
                        .padding(start = 24.dp, top = 8.dp, bottom = 32.dp, end = 24.dp)
                        .fillMaxWidth(),
                    onClick = { onEvent(LoginEvent.CreateAccount) },
                ) {
                    Text(text = stringResource(id = R.string.create_account))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginContentPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            LoginContent(
                state = LoginState(),
                snackbarHostState = remember { SnackbarHostState() },
                onEvent = {},
            )
        }
    }
}
