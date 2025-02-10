package com.bruno13palhano.mais1venda.ui.screens.authentication.create.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.model.shared.Address
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
        val emailError = !isEmailValid(email = email)

        reduce { copy(email = email, emailError = emailError) }
    }

    private fun companyNameChanged(companyName: String) = container.intent {
        val companyNameError = !isCompanyNameValid(companyName = companyName)

        reduce { copy(companyName = companyName, companyNameError = companyNameError) }
    }

    private fun phoneChanged(phone: String) = container.intent {
        val phoneError = !isPhoneValid(phone = phone)

        reduce { copy(phone = phone, phoneError = phoneError) }
    }

    private fun addressChanged(address: String) = container.intent {
        val addressError = !isAddressValid(address = address)

        reduce { copy(address = address, addressError = addressError) }
    }

    private fun passwordChanged(password: String) = container.intent {
        val passwordError = !isPasswordValid(password = password)
        val mismatchError = !isConfirmPasswordValid(
            password = password,
            confirmPassword = container.state.value.confirmPassword,
        )

        reduce {
            copy(
                password = password,
                passwordError = passwordError,
                mismatchError = mismatchError,
            )
        }
    }

    private fun confirmPasswordChanged(confirmPassword: String) = container.intent {
        val mismatchError = !isConfirmPasswordValid(
            password = container.state.value.password,
            confirmPassword = confirmPassword,
        )

        reduce { copy(confirmPassword = confirmPassword, mismatchError = mismatchError) }
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
        if (isFieldsInvalid()) {
            handleInputErrorState()
            return@intent
        }

        reduce { copy(isLoading = true) }

        val response = companyRepository.createAccount(
            email = container.state.value.email,
            password = container.state.value.password,
            companyName = container.state.value.companyName,
            phone = container.state.value.phone,
            address = Address(container.state.value.address, "", "", ""),
        )

        when (response) {
            is Resource.Success -> {
                if (response.data != null) {
                    postSideEffect(effect = CreateAccountSideEffect.NavigateToHome)
                    reduce { copy(isLoading = false) }
                }
            }

            // TODO: Handle errors
            is Resource.ResponseError -> {
                reduce { copy(isError = true, isLoading = false) }
                postSideEffect(
                    effect = CreateAccountSideEffect.ShowError(
                        codeError = CodeError.ERROR_CREATING_ACCOUNT,
                    ),
                )
            }

            // TODO: Handle errors
            is Resource.Error -> {
                reduce { copy(isError = true, isLoading = false) }
                postSideEffect(
                    effect = CreateAccountSideEffect.ShowError(
                        codeError = CodeError.ERROR_CREATING_ACCOUNT,
                    ),
                )
            }
        }
    }

    private fun navigateToLogin() = container.intent {
        postSideEffect(effect = CreateAccountSideEffect.NavigateBack)
    }

    private fun isFieldsInvalid(): Boolean {
        val emailError = !isEmailValid(email = container.state.value.email)
        val passwordError = !isPasswordValid(password = container.state.value.password)
        val mismatchError = !isConfirmPasswordValid(
            password = container.state.value.password,
            confirmPassword = container.state.value.confirmPassword,
        )
        val companyNameError = !isCompanyNameValid(companyName = container.state.value.companyName)
        val phoneError = !isPhoneValid(phone = container.state.value.phone)
        val addressError = !isAddressValid(address = container.state.value.address)

        return emailError || passwordError || mismatchError || companyNameError ||
            phoneError || addressError
    }

    private fun handleInputErrorState() = container.intent {
        val emailError = !isEmailValid(email = container.state.value.email)
        val passwordError = !isPasswordValid(password = container.state.value.password)
        val mismatchError = !isConfirmPasswordValid(
            password = container.state.value.password,
            confirmPassword = container.state.value.confirmPassword,
        )
        val companyNameError = !isCompanyNameValid(companyName = container.state.value.companyName)
        val phoneError = !isPhoneValid(phone = container.state.value.phone)
        val addressError = !isAddressValid(address = container.state.value.address)

        val codeErrors = getCodeErrors(
            emailError = emailError,
            passwordError = passwordError,
            mismatchError = mismatchError,
            companyNameError = companyNameError,
            phoneError = phoneError,
            addressError = addressError,
        )

        // If has more than one error set a generic error to fix the invalid fields
        var fieldsError = false
        if (codeErrors.size > 1) fieldsError = true

        reduce {
            copy(
                emailError = emailError,
                passwordError = passwordError,
                mismatchError = mismatchError,
                companyNameError = companyNameError,
                phoneError = phoneError,
                addressError = addressError,
                fieldsError = fieldsError,
            )
        }

        codeErrors.lastOrNull()?.let {
            postSideEffect(effect = CreateAccountSideEffect.ShowError(codeError = it))
        }
    }

    private fun getCodeErrors(
        emailError: Boolean,
        passwordError: Boolean,
        mismatchError: Boolean,
        companyNameError: Boolean,
        phoneError: Boolean,
        addressError: Boolean,
    ): List<CodeError> {
        val codeErrors = mutableListOf<CodeError>()

        if (emailError) codeErrors.add(CodeError.INVALID_EMAIL)

        if (passwordError) codeErrors.add(CodeError.INVALID_PASSWORD)

        if (mismatchError) codeErrors.add(CodeError.PASSWORD_MISMATCH)

        if (companyNameError) codeErrors.add(CodeError.INVALID_COMPANY_NAME)

        if (phoneError) codeErrors.add(CodeError.INVALID_PHONE)

        if (addressError) codeErrors.add(CodeError.INVALID_ADDRESS)

        if (codeErrors.size > 1) codeErrors.add(CodeError.INVALID_FIELDS)

        return codeErrors
    }
}
