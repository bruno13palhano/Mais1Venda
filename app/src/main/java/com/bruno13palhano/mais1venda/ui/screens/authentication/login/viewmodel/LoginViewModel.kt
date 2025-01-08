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
                postSideEffect(effect = LoginSideEffect.DismissKeyboard)
            }

        private fun forgotPassword() =
            container.intent {
                postSideEffect(effect = LoginSideEffect.NavigateToForgotPassword)
            }

        private fun createAccount() =
            container.intent {
                postSideEffect(effect = LoginSideEffect.NavigateToCreateAccount)
            }

        private fun login() =
            container.intent {
                val email = state.value.email
                val password = state.value.password

                if (!isEmailValid(email = email)) {
                    return@intent postSideEffect(
                        effect = LoginSideEffect.ShowError(message = "Invalid email format"),
                    )
                }

                if (!isPasswordValid(password = password)) {
                    return@intent postSideEffect(
                        effect =
                            LoginSideEffect.ShowError(
                                message = "Password must be at least 8 characters long",
                            ),
                    )
                }

                reduce { copy(isLoading = true) }
                val result =
                    companyRepository.authenticate(email = email, password = password)
                if (result) {
                    postSideEffect(effect = LoginSideEffect.NavigateToHome)
                    reduce { copy(isLoading = false) }
                } else {
                    reduce { copy(isLoading = false) }
                    postSideEffect(
                        effect =
                            LoginSideEffect.ShowError(
                                message = "Email or password is incorrect",
                            ),
                    )
                }
            }

        private fun isEmailValid(email: String): Boolean {
            val emailRegex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
            return email.matches(emailRegex.toRegex())
        }

        private fun isPasswordValid(password: String): Boolean {
            return password.length >= 8
        }
    }
