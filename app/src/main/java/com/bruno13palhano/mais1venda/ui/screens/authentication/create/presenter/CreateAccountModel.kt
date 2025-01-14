package com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError

@Immutable
internal data class CreateAccountState(
    val email: String = "",
    val companyName: String = "",
    val phone: String = "",
    val address: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: Boolean = false,
    val companyNameError: Boolean = false,
    val phoneError: Boolean = false,
    val addressError: Boolean = false,
    val passwordError: Boolean = false,
    val mismatchError: Boolean = false,
    val emptyFieldsError: Boolean = false,
    val passwordVisibility: Boolean = false,
    val confirmPasswordVisibility: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)

@Immutable
internal sealed interface CreateAccountEvent {
    data class EmailChanged(val email: String) : CreateAccountEvent

    data class CompanyNameChanged(val companyName: String) : CreateAccountEvent

    data class PhoneChanged(val phone: String) : CreateAccountEvent

    data class AddressChanged(val address: String) : CreateAccountEvent

    data class PasswordChanged(val password: String) : CreateAccountEvent

    data class ConfirmPasswordChanged(val confirmPassword: String) : CreateAccountEvent

    data object TogglePasswordVisibility : CreateAccountEvent

    data object ToggleConfirmPasswordVisibility : CreateAccountEvent

    data object DismissKeyboard : CreateAccountEvent

    data object CreateAccount : CreateAccountEvent

    data object NavigateBack : CreateAccountEvent
}

@Immutable
internal sealed interface CreateAccountSideEffect {
    data object DismissKeyboard : CreateAccountSideEffect

    data object NavigateToHome : CreateAccountSideEffect

    data object NavigateBack : CreateAccountSideEffect

    data class ShowError(val codeError: CodeError) : CreateAccountSideEffect
}
