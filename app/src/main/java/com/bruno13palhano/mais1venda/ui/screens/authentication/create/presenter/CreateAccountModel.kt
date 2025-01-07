package com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter

import androidx.compose.runtime.Immutable

@Immutable
internal data class CreateAccountState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)

@Immutable
internal sealed interface CreateAccountEvent {
    data object CreateAccount : CreateAccountEvent

    data object NavigateToHome : CreateAccountEvent
}

@Immutable
internal sealed interface CreateAccountSideEffect {
    data object NavigateToHome : CreateAccountSideEffect

    data class ShowError(val message: String) : CreateAccountSideEffect
}
