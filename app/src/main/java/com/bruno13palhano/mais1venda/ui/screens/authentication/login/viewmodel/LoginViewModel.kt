package com.bruno13palhano.mais1venda.ui.screens.authentication.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.data.repository.CompanyRepository
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginEvent
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginSideEffect
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginState
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.isEmailValid
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.isPasswordValid
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val companyRepository: CompanyRepository,
) : ViewModel() {
    val container = Container<LoginState, LoginSideEffect>(
        initialState = LoginState(),
        scope = viewModelScope,
    )

    fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> emailChanged(event.email)

            is LoginEvent.PasswordChanged -> passwordChanged(event.password)

            LoginEvent.TogglePasswordVisibility -> togglePasswordVisibility()

            LoginEvent.DismissKeyboard -> dismissKeyboard()

            LoginEvent.ForgotPassword -> forgotPassword()

            LoginEvent.CreateAccount -> createAccount()

            LoginEvent.Login -> login()
        }
    }

    private fun emailChanged(email: String) = container.intent {
        reduce { copy(email = email) }
    }

    private fun passwordChanged(password: String) = container.intent {
        reduce { copy(password = password) }
    }

    private fun togglePasswordVisibility() = container.intent {
        reduce { copy(passwordVisibility = !passwordVisibility) }
    }

    private fun dismissKeyboard() = container.intent {
        postSideEffect(effect = LoginSideEffect.DismissKeyboard)
    }

    private fun forgotPassword() = container.intent {
        postSideEffect(effect = LoginSideEffect.NavigateToForgotPassword)
    }

    private fun createAccount() = container.intent {
        postSideEffect(effect = LoginSideEffect.NavigateToCreateAccount)
    }

    private fun login() = container.intent {
        val email = state.value.email
        val password = state.value.password

        if (!isEmailValid(email = email)) {
            reduce { copy(emailError = true) }
            postSideEffect(
                effect = LoginSideEffect.ShowError(codeError = CodeError.INVALID_EMAIL),
            )

            return@intent
        }

        if (!isPasswordValid(password = password)) {
            reduce { copy(passwordError = true) }
            postSideEffect(
                effect = LoginSideEffect.ShowError(codeError = CodeError.INVALID_PASSWORD),
            )

            return@intent
        }

        reduce { copy(emailError = false, passwordError = false, isLoading = true) }

        when (val result = companyRepository.login(email = email, password = password)) {
            is Resource.Success -> {
                result.data?.let { success ->
                    if (success) {
                        postSideEffect(effect = LoginSideEffect.NavigateToHome)
                        reduce { copy(isLoading = false, isError = false) }
                    } else {
                        reduce { copy(isLoading = false, isError = true) }
                        postSideEffect(
                            effect = LoginSideEffect.ShowError(
                                codeError = CodeError.INVALID_CREDENTIALS,
                            ),
                        )
                    }
                }
            }

            is Resource.ResponseError -> {
                reduce { copy(isLoading = false, isError = true) }
                postSideEffect(
                    effect = LoginSideEffect.ShowError(codeError = CodeError.INVALID_CREDENTIALS),
                )
            }

            is Resource.Error -> {
                reduce { copy(isLoading = false, isError = true) }
                postSideEffect(
                    effect = LoginSideEffect.ShowError(codeError = CodeError.INVALID_CREDENTIALS),
                )
            }
        }
    }
}
