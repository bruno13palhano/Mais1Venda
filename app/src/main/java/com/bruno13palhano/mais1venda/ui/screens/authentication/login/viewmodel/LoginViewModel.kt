package com.bruno13palhano.mais1venda.ui.screens.authentication.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.data.repository.CompanyRepository
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginEvent
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginSideEffect
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel
    @Inject
    constructor(
        private val companyRepository: CompanyRepository,
    ) : ViewModel() {
        val container =
            Container<LoginState, LoginSideEffect>(
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

        private fun emailChanged(email: String) =
            container.intent {
                reduce { copy(email = email) }
            }

        private fun passwordChanged(password: String) =
            container.intent {
                reduce { copy(password = password) }
            }

        private fun togglePasswordVisibility() =
            container.intent {
                reduce { copy(passwordVisibility = !passwordVisibility) }
            }

        private fun dismissKeyboard() =
            container.intent {
                postSideEffect(LoginSideEffect.DismissKeyboard)
            }

        private fun forgotPassword() =
            container.intent {
                postSideEffect(LoginSideEffect.NavigateToForgotPassword)
            }

        private fun createAccount() =
            container.intent {
                postSideEffect(LoginSideEffect.NavigateToCreateAccount)
            }

        private fun login() =
            container.intent {
                reduce { copy(isLoading = true) }
                val result = companyRepository.authenticate(state.value.email, state.value.password)
                if (result) {
                    postSideEffect(LoginSideEffect.NavigateToHome)
                    reduce { copy(isLoading = false) }
                } else {
                    reduce { copy(isLoading = false) }
                    postSideEffect(LoginSideEffect.ShowError(message = "Email or password is incorrect"))
                }
            }
    }
