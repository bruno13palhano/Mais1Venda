package com.bruno13palhano.mais1venda.ui.screens.authentication.create.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.data.repository.CompanyRepository
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountEvent
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountSideEffect
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountState
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.isConfirmPasswordValid
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.isEmailValid
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.isPasswordValid
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.isPhoneValid
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class CreateAccountViewModel @Inject constructor(
    initialState: CreateAccountState,
    private val companyRepository: CompanyRepository,
) : ViewModel() {
    val container = Container<CreateAccountState, CreateAccountSideEffect>(
        initialState = initialState,
        scope = viewModelScope,
    )

    fun handleEvent(event: CreateAccountEvent) {
        when (event) {
            is CreateAccountEvent.EmailChanged -> emailChanged(email = event.email)

            is CreateAccountEvent.CompanyNameChanged -> companyNameChanged(
                companyName = event.companyName,
            )

            is CreateAccountEvent.PhoneChanged -> phoneChanged(phone = event.phone)

            is CreateAccountEvent.AddressChanged -> addressChanged(address = event.address)

            is CreateAccountEvent.PasswordChanged -> passwordChanged(password = event.password)

            is CreateAccountEvent.ConfirmPasswordChanged -> confirmPasswordChanged(
                confirmPassword = event.confirmPassword,
            )

            CreateAccountEvent.TogglePasswordVisibility -> togglePasswordVisibility()

            CreateAccountEvent.ToggleConfirmPasswordVisibility -> toggleConfirmPasswordVisibility()

            CreateAccountEvent.DismissKeyboard -> dismissKeyboard()

            CreateAccountEvent.CreateAccount -> createAccount()

            CreateAccountEvent.NavigateBack -> navigateToLogin()
        }
    }

    private fun emailChanged(email: String) = container.intent {
        reduce { copy(email = email) }
    }

    private fun companyNameChanged(companyName: String) = container.intent {
        reduce { copy(companyName = companyName) }
    }

    private fun phoneChanged(phone: String) = container.intent {
        reduce { copy(phone = phone) }
    }

    private fun addressChanged(address: String) = container.intent {
        reduce { copy(address = address) }
    }

    private fun passwordChanged(password: String) = container.intent {
        reduce { copy(password = password) }
    }

    private fun confirmPasswordChanged(confirmPassword: String) = container.intent {
        reduce { copy(confirmPassword = confirmPassword) }
    }

    private fun togglePasswordVisibility() = container.intent {
        reduce { copy(passwordVisibility = !passwordVisibility) }
    }

    private fun toggleConfirmPasswordVisibility() = container.intent {
        reduce { copy(confirmPasswordVisibility = !confirmPasswordVisibility) }
    }

    private fun dismissKeyboard() = container.intent {
        postSideEffect(effect = CreateAccountSideEffect.DismissKeyboard)
    }

    private fun createAccount() = container.intent {
        if (!validateInput()) return@intent

        reduce { copy(isLoading = true, isError = false) }

        val response = companyRepository.createAccount(
            email = container.state.value.email,
            password = container.state.value.password,
            companyName = container.state.value.companyName,
            phone = container.state.value.phone,
            address = container.state.value.address,
        )

        reduce { copy(isLoading = false) }

        if (response) {
            postSideEffect(effect = CreateAccountSideEffect.NavigateToHome)
        } else {
            reduce { copy(isError = true) }
            postSideEffect(
                effect = CreateAccountSideEffect.ShowError(
                    codeError = CodeError.ERROR_CREATING_ACCOUNT,
                ),
            )
        }
    }

    private fun navigateToLogin() = container.intent {
        postSideEffect(effect = CreateAccountSideEffect.NavigateBack)
    }

    private fun validateInput(): Boolean {
        if (!isEmailValid(email = container.state.value.email)) {
            setErrorMessage(
                codeError = CodeError.INVALID_EMAIL,
                newState = container.state.value.copy(emailError = true),
            )
            return false
        }

        if (!isPasswordValid(password = container.state.value.password)) {
            setErrorMessage(
                codeError = CodeError.INVALID_PASSWORD,
                newState = container.state.value.copy(passwordError = true),
            )
            return false
        }

        if (
            !isConfirmPasswordValid(
                password = container.state.value.password,
                confirmPassword = container.state.value.confirmPassword,
            )
        ) {
            setErrorMessage(
                codeError = CodeError.PASSWORD_MISMATCH,
                newState = container.state.value.copy(mismatchError = true),
            )
            return false
        }

        if (container.state.value.companyName.isBlank()) {
            setErrorMessage(
                codeError = CodeError.INVALID_COMPANY_NAME,
                newState = container.state.value.copy(companyNameError = true),
            )
            return false
        }

        if (!isPhoneValid(phone = container.state.value.phone)) {
            setErrorMessage(
                codeError = CodeError.INVALID_PHONE,
                newState = container.state.value.copy(phoneError = true),
            )
            return false
        }

        if (container.state.value.address.isBlank()) {
            setErrorMessage(
                codeError = CodeError.INVALID_ADDRESS,
                newState = container.state.value.copy(addressError = true),
            )
            return false
        }

        return true
    }

    private fun setErrorMessage(codeError: CodeError, newState: CreateAccountState) =
        container.intent {
            reduce { newState }
            postSideEffect(effect = CreateAccountSideEffect.ShowError(codeError = codeError))
        }
}
