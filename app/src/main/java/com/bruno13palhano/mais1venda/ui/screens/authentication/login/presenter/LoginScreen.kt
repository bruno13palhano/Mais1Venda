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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.mais1venda.ui.screens.components.CircularProgress
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.viewmodel.LoginViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle

@Composable
internal fun LoginRoute(
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state = viewModel.container.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    LaunchedEffect(effect) {
        effect.collect { sideEffect ->
            when (sideEffect) {
                LoginSideEffect.NavigateToHome -> navigateToHome()

                is LoginSideEffect.ShowError -> {
                    println("Error: ${sideEffect.message}")
                }
            }
        }
    }

    LoginContent(
        state = state.value,
        onEvent = viewModel::handleEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginContent(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = "Login") },
            )
        },
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
                OutlinedTextField(
                    modifier =
                        Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                    value = email,
                    onValueChange = { newEmail ->
                        email = newEmail
                        onEvent(LoginEvent.EmailChanged(email = newEmail))
                    },
                    label = { Text(text = "Email") },
                )

                OutlinedTextField(
                    modifier =
                        Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                    value = password,
                    onValueChange = { newPassword ->
                        password = newPassword
                        onEvent(LoginEvent.PasswordChanged(password = newPassword))
                    },
                    label = { Text(text = "Password") },
                )
            }
        }
    }
}
