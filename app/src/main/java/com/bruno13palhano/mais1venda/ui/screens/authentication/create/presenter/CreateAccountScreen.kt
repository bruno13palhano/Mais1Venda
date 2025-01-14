package com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.viewmodel.CreateAccountViewModel
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.getErrorMessages
import com.bruno13palhano.mais1venda.ui.screens.components.CircularProgress
import com.bruno13palhano.mais1venda.ui.screens.components.CustomIntegerField
import com.bruno13palhano.mais1venda.ui.screens.components.CustomPasswordTextField
import com.bruno13palhano.mais1venda.ui.screens.components.CustomTextField
import com.bruno13palhano.mais1venda.ui.screens.shared.clickableWithoutRipple
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
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::handleEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateAccountContent(
    state: CreateAccountState,
    snackbarHostState: SnackbarHostState,
    onEvent: (event: CreateAccountEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .clickableWithoutRipple { onEvent(CreateAccountEvent.DismissKeyboard) }
            .consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.create_account)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(CreateAccountEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back),
                        )
                    }
                },
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
                    onValueChange = { email ->
                        onEvent(
                            CreateAccountEvent.EmailChanged(email = email),
                        )
                    },
                    label = stringResource(R.string.email),
                    placeholder = stringResource(R.string.enter_email),
                    isError = state.emailError,
                )

                CustomTextField(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    value = state.companyName,
                    onValueChange = { companyName ->
                        onEvent(
                            CreateAccountEvent.CompanyNameChanged(companyName = companyName),
                        )
                    },
                    label = stringResource(R.string.company_name),
                    placeholder = stringResource(R.string.enter_company_name),
                    isError = state.companyNameError,
                )

                CustomIntegerField(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    value = state.phone,
                    onValueChange = { phone ->
                        onEvent(
                            CreateAccountEvent.PhoneChanged(phone = phone),
                        )
                    },
                    label = stringResource(R.string.phone),
                    placeholder = stringResource(R.string.enter_phone),
                    isError = state.phoneError,
                )

                CustomTextField(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    value = state.address,
                    onValueChange = { address ->
                        onEvent(
                            CreateAccountEvent.AddressChanged(address = address),
                        )
                    },
                    label = stringResource(R.string.address),
                    placeholder = stringResource(R.string.enter_address),
                    isError = state.addressError,
                )

                CustomPasswordTextField(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    visibility = state.passwordVisibility,
                    value = state.password,
                    onValueChange = { password ->
                        onEvent(
                            CreateAccountEvent.PasswordChanged(password = password),
                        )
                    },
                    togglePasswordVisibility = {
                        onEvent(
                            CreateAccountEvent.TogglePasswordVisibility,
                        )
                    },
                    label = stringResource(R.string.password),
                    placeholder = stringResource(R.string.enter_password),
                    isError = state.passwordError,
                )

                CustomPasswordTextField(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    visibility = state.confirmPasswordVisibility,
                    value = state.confirmPassword,
                    onValueChange = { confirmPassword ->
                        onEvent(
                            CreateAccountEvent.ConfirmPasswordChanged(
                                confirmPassword = confirmPassword,
                            ),
                        )
                    },
                    togglePasswordVisibility = {
                        onEvent(
                            CreateAccountEvent.ToggleConfirmPasswordVisibility,
                        )
                    },
                    label = stringResource(R.string.confirm_password),
                    placeholder = stringResource(R.string.enter_confirm_password),
                    isError = state.mismatchError,
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    Button(
                        modifier = Modifier
                            .padding(start = 24.dp, top = 32.dp, bottom = 8.dp, end = 24.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        onClick = { onEvent(CreateAccountEvent.CreateAccount) },
                    ) {
                        Text(text = stringResource(id = R.string.create_account))
                    }
                }
            }
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
                snackbarHostState = remember { SnackbarHostState() },
                onEvent = {},
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateAccountWithErrorContentPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            CreateAccountContent(
                state = CreateAccountState(emailError = true),
                snackbarHostState = remember { SnackbarHostState() },
                onEvent = {},
            )
        }
    }
}
