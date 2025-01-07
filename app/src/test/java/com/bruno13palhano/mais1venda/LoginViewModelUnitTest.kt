package com.bruno13palhano.mais1venda

import com.bruno13palhano.mais1venda.repository.TestCompanyRepository
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginEvent
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginSideEffect
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.viewmodel.LoginViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class LoginViewModelUnitTest {
    private lateinit var companyRepository: TestCompanyRepository

    @Before
    fun setUp() {
        companyRepository = TestCompanyRepository()
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `test email changed`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        val expected = "some@email"

        val collectJob = launch {
            viewModel.container.state.collect()
        }

        viewModel.handleEvent(LoginEvent.EmailChanged(email = expected))
        advanceUntilIdle()

        assertThat(expected).isEqualTo(viewModel.container.state.value.email)

        collectJob.cancel()
    }

    @Test
    fun `test password changed`() = runTest {
        val viewModel = LoginViewModel(TestCompanyRepository())

        val expected = "somePassword"

        val collectJob = launch {
            viewModel.container.state.collect()
        }

        viewModel.handleEvent(LoginEvent.PasswordChanged(password = expected))
        advanceUntilIdle()

        assertThat(viewModel.container.state.value.password).isEqualTo(expected)

        collectJob.cancel()
    }

    @Test
    fun `test toggle password visibility`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        val collectJob = launch {
            viewModel.container.state.collect()
        }

        viewModel.handleEvent(LoginEvent.TogglePasswordVisibility)
        advanceUntilIdle()

        assertThat(viewModel.container.state.value.passwordVisibility).isEqualTo(true)

        collectJob.cancel()
    }

    @Test
    fun `test dismiss keyboard`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        val collectJob = launch {
            viewModel.container.sideEffect.collect {
                assertThat(it).isEqualTo(LoginSideEffect.DismissKeyboard)
            }
        }

        viewModel.handleEvent(LoginEvent.DismissKeyboard)
        advanceUntilIdle()

        collectJob.cancel()
    }

    @Test
    fun `test forgot password`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        val collectJob = launch {
            viewModel.container.sideEffect.collect {
                assertThat(it).isEqualTo(LoginSideEffect.NavigateToForgotPassword)
            }
        }

        viewModel.handleEvent(LoginEvent.ForgotPassword)
        advanceUntilIdle()

        collectJob.cancel()
    }

    @Test
    fun `test create account`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        val collectJob = launch {
            viewModel.container.sideEffect.collect {
                println(it)
                assertThat(it).isEqualTo(LoginSideEffect.NavigateToCreateAccount)
            }
        }

        viewModel.handleEvent(LoginEvent.CreateAccount)
        advanceUntilIdle()

        collectJob.cancel()
    }

    @Test
    fun `test login`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        val collectJob = launch {
            viewModel.container.sideEffect.collect {
                assertThat(it).isEqualTo(LoginSideEffect.NavigateToHome)
            }
        }

        viewModel.handleEvent(LoginEvent.Login)
        advanceUntilIdle()

        collectJob.cancel()
    }
}
