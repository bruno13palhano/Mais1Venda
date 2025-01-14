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
        if (!isEmailValid(email = email)) {
            if (container.state.value.emailError) return@intent

            setErrorMessage(
                codeError = CodeError.INVALID_EMAIL,
                newState = container.state.value.copy(emailError = true),
            )
        } else {
            reduce { copy(emailError = false) }
        }
    }

    private fun companyNameChanged(companyName: String) = container.intent {
        reduce { copy(companyName = companyName) }
        if (companyName.isBlank()) {
            if (container.state.value.companyNameError) return@intent

            setErrorMessage(
                codeError = CodeError.INVALID_COMPANY_NAME,
                newState = container.state.value.copy(mismatchError = true),
            )
        } else {
            reduce { copy(companyNameError = false) }
        }
    }

    private fun phoneChanged(phone: String) = container.intent {
        reduce { copy(phone = phone) }
        if (!isPhoneValid(phone = phone)) {
            if (container.state.value.phoneError) return@intent

            setErrorMessage(
                codeError = CodeError.INVALID_PHONE,
                newState = container.state.value.copy(phoneError = true),
            )
        } else {
            reduce { copy(phoneError = false) }
        }
    }

    private fun addressChanged(address: String) = container.intent {
        reduce { copy(address = address) }
        if (address.isBlank()) {
            if (container.state.value.addressError) return@intent

            setErrorMessage(
                codeError = CodeError.INVALID_ADDRESS,
                newState = container.state.value.copy(mismatchError = true),
            )
        } else {
            reduce { copy(addressError = false) }
        }
    }

    private fun passwordChanged(password: String) = container.intent {
        reduce { copy(password = password) }
        if (!isPasswordValid(password = password)) {
            if (container.state.value.passwordError) return@intent

            setErrorMessage(
                codeError = CodeError.INVALID_PASSWORD,
                newState = container.state.value.copy(passwordError = true),
            )
        } else {
            reduce { copy(passwordError = false) }
        }
    }

    private fun confirmPasswordChanged(confirmPassword: String) = container.intent {
        reduce { copy(confirmPassword = confirmPassword) }
        if (!isConfirmPasswordValid(
                password = container.state.value.password,
                confirmPassword = confirmPassword,
            )
        ) {
            if (container.state.value.mismatchError) return@intent

            setErrorMessage(
                codeError = CodeError.PASSWORD_MISMATCH,
                newState = container.state.value.copy(mismatchError = true),
            )
        } else {
            reduce { copy(mismatchError = false) }
        }
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
        if (!validateInput()) {
            setErrorMessage(
                codeError = CodeError.EMPTY_FIELDS,
                newState = container.state.value.copy(isError = true),
            )
            return@intent
        }

        reduce { copy(isLoading = true, isError = false) }

        val response = companyRepository.createAccount(
            email = container.state.value.email,
            password = container.state.value.password,
            companyName = container.state.value.companyName,
            phone = container.state.value.phone,
            address = container.state.value.address,
        )

        if (response) {
            postSideEffect(effect = CreateAccountSideEffect.NavigateToHome)
            reduce { copy(isLoading = false) }
        } else {
            reduce { copy(isError = true, isLoading = false) }
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
        return isEmailValid(email = container.state.value.email) &&
            isPasswordValid(password = container.state.value.password) &&
            isConfirmPasswordValid(
                password = container.state.value.password,
                confirmPassword = container.state.value.confirmPassword,
            ) &&
            container.state.value.companyName.isNotBlank() &&
            isPhoneValid(phone = container.state.value.phone) &&
            container.state.value.address.isNotBlank()
    }

    private fun setErrorMessage(codeError: CodeError, newState: CreateAccountState) =
        container.intent {
            reduce { newState }
            postSideEffect(effect = CreateAccountSideEffect.ShowError(codeError = codeError))
        }
}
