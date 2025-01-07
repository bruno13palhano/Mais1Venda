package com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter

import androidx.compose.runtime.Immutable

@Immutable
internal data class LoginState(
    val email: String = "",
    val password: String = "",
    val passwordVisibility: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)

@Immutable
internal sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent

    data class PasswordChanged(val password: String) : LoginEvent

    data object TogglePasswordVisibility : LoginEvent

    data object DismissKeyboard : LoginEvent

    data object ForgotPassword : LoginEvent

    data object Login : LoginEvent

    data object CreateAccount : LoginEvent
}

@Immutable
internal sealed interface LoginSideEffect {
    data object NavigateToHome : LoginSideEffect

    data object NavigateToForgotPassword : LoginSideEffect

    data object NavigateToCreateAccount : LoginSideEffect

    data object DismissKeyboard : LoginSideEffect

    data class ShowError(val message: String) : LoginSideEffect
}
