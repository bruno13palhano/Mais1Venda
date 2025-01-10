package com.bruno13palhano.mais1venda.ui.screens.authentication.create.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountEvent
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountSideEffect
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class CreateAccountViewModel
    @Inject
    constructor() : ViewModel() {
        val container =
            Container<CreateAccountState, CreateAccountSideEffect>(
                initialState = CreateAccountState(),
                scope = viewModelScope,
            )

        fun handleEvent(event: CreateAccountEvent) {
            when (event) {
                is CreateAccountEvent.EmailChanged -> emailChanged(email = event.email)

                is CreateAccountEvent.CompanyNameChanged -> companyNameChanged(companyName = event.companyName)

                is CreateAccountEvent.PhoneChanged -> phoneChanged(phone = event.phone)

                is CreateAccountEvent.AddressChanged -> addressChanged(address = event.address)

                is CreateAccountEvent.PasswordChanged -> passwordChanged(password = event.password)

                is CreateAccountEvent.ConfirmPasswordChanged -> confirmPasswordChanged(confirmPassword = event.confirmPassword)

                CreateAccountEvent.TogglePasswordVisibility -> togglePasswordVisibility()

                CreateAccountEvent.ToggleConfirmPasswordVisibility -> toggleConfirmPasswordVisibility()

                CreateAccountEvent.DismissKeyboard -> dismissKeyboard()

                CreateAccountEvent.CreateAccount -> createAccount()

                CreateAccountEvent.NavigateBack -> navigateToLogin()
            }
        }

        private fun emailChanged(email: String) =
            container.intent {
                reduce { copy(email = email) }
            }

        private fun companyNameChanged(companyName: String) =
            container.intent {
                reduce { copy(companyName = companyName) }
            }

        private fun phoneChanged(phone: String) =
            container.intent {
                reduce { copy(phone = phone) }
            }

        private fun addressChanged(address: String) =
            container.intent {
                reduce { copy(address = address) }
            }

        private fun passwordChanged(password: String) =
            container.intent {
                reduce { copy(password = password) }
            }

        private fun confirmPasswordChanged(confirmPassword: String) =
            container.intent {
                reduce { copy(confirmPassword = confirmPassword) }
            }

        private fun togglePasswordVisibility() =
            container.intent {
                reduce { copy(passwordVisibility = !passwordVisibility) }
            }

        private fun toggleConfirmPasswordVisibility() =
            container.intent {
                reduce { copy(confirmPasswordVisibility = !confirmPasswordVisibility) }
            }

        private fun dismissKeyboard() =
            container.intent {
                postSideEffect(effect = CreateAccountSideEffect.DismissKeyboard)
            }

        private fun createAccount() =
            container.intent {
                postSideEffect(effect = CreateAccountSideEffect.NavigateToHome)
            }

        private fun navigateToLogin() =
            container.intent {
                postSideEffect(effect = CreateAccountSideEffect.NavigateBack)
            }
    }
