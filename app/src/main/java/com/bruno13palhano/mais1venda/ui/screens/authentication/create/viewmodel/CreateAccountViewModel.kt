package com.bruno13palhano.mais1venda.ui.screens.authentication.create.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.data.repository.CompanyRepository
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountEvent
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountSideEffect
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountState
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.isAddressValid
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.isCompanyNameValid
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

        if (isEmailValid(email = email)) {
            reduce { copy(emailError = false) }
        } else {
            reduce { copy(emailError = true) }
        }
    }

    private fun companyNameChanged(companyName: String) = container.intent {
        reduce { copy(companyName = companyName) }

        if (isCompanyNameValid(companyName)) {
            reduce { copy(companyNameError = false) }
        } else {
            reduce { copy(companyNameError = true) }
        }
    }

    private fun phoneChanged(phone: String) = container.intent {
        reduce { copy(phone = phone) }

        if (isPhoneValid(phone = phone)) {
            reduce { copy(phoneError = false) }
        } else {
            reduce { copy(phoneError = true) }
        }
    }

    private fun addressChanged(address: String) = container.intent {
        reduce { copy(address = address) }

        if (address.isNotBlank()) {
            reduce { copy(addressError = false) }
        } else {
            reduce { copy(addressError = true) }
        }
    }

    private fun passwordChanged(password: String) = container.intent {
        reduce { copy(password = password) }
        if (isPasswordValid(password = password)) {
            reduce { copy(passwordError = false) }

            if (isConfirmPasswordValid(
                    password = password,
                    confirmPassword = container.state.value.confirmPassword,
                )
            ) {
                reduce { copy(mismatchError = false) }
            }
        } else {
            reduce { copy(passwordError = true) }
        }
    }

    private fun confirmPasswordChanged(confirmPassword: String) = container.intent {
        reduce { copy(confirmPassword = confirmPassword) }
        if (isConfirmPasswordValid(
                password = container.state.value.password,
                confirmPassword = confirmPassword,
            )
        ) {
            reduce { copy(mismatchError = false) }
        } else {
            reduce { copy(mismatchError = true) }
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
        if (!isFieldsValid()) {
            handleInputErrorState()
            return@intent
        }

        reduce { copy(isLoading = true) }

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

    private fun isFieldsValid(): Boolean {
        return isEmailValid(email = container.state.value.email) &&
            isPasswordValid(password = container.state.value.password) &&
            isConfirmPasswordValid(
                password = container.state.value.password,
                confirmPassword = container.state.value.confirmPassword,
            ) &&
            isCompanyNameValid(companyName = container.state.value.companyName) &&
            isPhoneValid(phone = container.state.value.phone) &&
            isAddressValid(address = container.state.value.address)
    }

    private fun handleInputErrorState() {
        val codeErrors = mutableListOf<CodeError>()
        var stateError: CreateAccountState?

        if (!isEmailValid(email = container.state.value.email)) {
            codeErrors.add(CodeError.INVALID_EMAIL)
            stateError = container.state.value.copy(emailError = true)
        }

        if (!isPasswordValid(password = container.state.value.password)) {
            codeErrors.add(CodeError.INVALID_PASSWORD)
            stateError = container.state.value.copy(passwordError = true)
        }

        if (!isConfirmPasswordValid(
                password = container.state.value.password,
                confirmPassword = container.state.value.confirmPassword,
            )
        ) {
            codeErrors.add(CodeError.PASSWORD_MISMATCH)
            stateError = container.state.value.copy(mismatchError = true)
        } else {
            stateError = container.state.value.copy(mismatchError = false)
        }

        if (!isCompanyNameValid(companyName = container.state.value.companyName)) {
            codeErrors.add(CodeError.INVALID_COMPANY_NAME)
            stateError = container.state.value.copy(companyNameError = true)
        }

        if (!isPhoneValid(phone = container.state.value.phone)) {
            codeErrors.add(CodeError.INVALID_PHONE)
            stateError = container.state.value.copy(phoneError = true)
        }

        if (!isAddressValid(address = container.state.value.address)) {
            codeErrors.add(CodeError.INVALID_ADDRESS)
            stateError = container.state.value.copy(addressError = true)
        }

        // If has more than one error set a generic error to fix the invalid fields
        if (codeErrors.size > 1) {
            setErrorMessage(
                codeError = CodeError.INVALID_FIELDS,
                newState = container.state.value.copy(fieldsError = true),
            )
        } else {
            codeErrors.lastOrNull()?.let {
                setErrorMessage(
                    codeError = it,
                    newState = stateError,
                )
            }
        }
    }

    private fun setErrorMessage(codeError: CodeError, newState: CreateAccountState) =
        container.intent {
            reduce { newState }
            postSideEffect(effect = CreateAccountSideEffect.ShowError(codeError = codeError))
        }
}
