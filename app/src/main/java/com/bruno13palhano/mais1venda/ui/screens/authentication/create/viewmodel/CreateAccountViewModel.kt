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
                CreateAccountEvent.CreateAccount -> createAccount()
                CreateAccountEvent.NavigateToHome -> navigateToHome()
            }
        }

        private fun navigateToHome() =
            container.intent {
                TODO("Not yet implemented")
            }

        private fun createAccount() =
            container.intent {
                TODO("Not yet implemented")
            }
    }
