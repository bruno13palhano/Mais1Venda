package com.bruno13palhano.mais1venda

import com.bruno13palhano.mais1venda.repository.TestCompanyRepository
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginEvent
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginSideEffect
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.viewmodel.LoginViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
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
    fun `EmailChanged Event should update email`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        val expected = "some@email"

        val collectJob =
            launch {
                viewModel.container.state.collect()
            }

        viewModel.handleEvent(LoginEvent.EmailChanged(email = expected))
        advanceUntilIdle()

        assertThat(expected).isEqualTo(viewModel.container.state.value.email)

        collectJob.cancel()
    }

    @Test
    fun `PasswordChanged Event should update password`() = runTest {
        val viewModel = LoginViewModel(TestCompanyRepository())

        val expected = "somePassword"

        val collectJob =
            launch {
                viewModel.container.state.collect()
            }

        viewModel.handleEvent(LoginEvent.PasswordChanged(password = expected))
        advanceUntilIdle()

        assertThat(viewModel.container.state.value.password).isEqualTo(expected)

        collectJob.cancel()
    }

    @Test
    fun `TogglePasswordVisibility Event should toggle passwordVisibility`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        val collectJob =
            launch {
                viewModel.container.state.collect()
            }

        viewModel.handleEvent(LoginEvent.TogglePasswordVisibility)
        advanceUntilIdle()

        assertThat(viewModel.container.state.value.passwordVisibility).isEqualTo(true)

        collectJob.cancel()
    }

    @Test
    fun `DismissKeyboard Event should emit DismissKeyboard side effect`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        val collectJob =
            launch {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(LoginSideEffect.DismissKeyboard)
                }
            }

        viewModel.handleEvent(LoginEvent.DismissKeyboard)
        advanceUntilIdle()

        collectJob.cancel()
    }

    @Test
    fun `ForgotPassword Event should emit NavigateToForgotPassword side effect`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        val collectJob =
            launch {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(LoginSideEffect.NavigateToForgotPassword)
                }
            }

        viewModel.handleEvent(LoginEvent.ForgotPassword)
        advanceUntilIdle()

        collectJob.cancel()
    }

    @Test
    fun `CreateAccount Event should emit NavigateToCreateAccount side effect`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        val collectJob =
            launch {
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
    fun `Login Event with invalid email should set emailError to true`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        viewModel.handleEvent(LoginEvent.EmailChanged(email = "someemail.com"))
        viewModel.handleEvent(LoginEvent.PasswordChanged(password = "somePassword"))

        val collectJob =
            launch {
                viewModel.container.state.collectLatest {
                    assertThat(it.emailError).isEqualTo(true)
                }
            }

        viewModel.handleEvent(LoginEvent.Login)
        advanceUntilIdle()

        collectJob.cancel()
    }

    @Test
    fun `Login Event with invalid password should set passwordError to true`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        viewModel.handleEvent(LoginEvent.EmailChanged(email = "some@email.com"))
        viewModel.handleEvent(LoginEvent.PasswordChanged(password = "1234567"))

        val collectJob =
            launch {
                viewModel.container.state.collectLatest {
                    println(it)
                    assertThat(it.passwordError).isEqualTo(true)
                }
            }

        viewModel.handleEvent(LoginEvent.Login)
        advanceUntilIdle()

        collectJob.cancel()
    }

    @Test
    fun `Login Event with invalid email should emit ShowError side effect`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        viewModel.handleEvent(LoginEvent.EmailChanged(email = "someemail.com"))
        viewModel.handleEvent(LoginEvent.PasswordChanged(password = "somePassword"))

        val collectJob =
            launch {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(
                        LoginSideEffect.ShowError(
                            message = "Invalid email format",
                        ),
                    )
                }
            }

        viewModel.handleEvent(LoginEvent.Login)
        advanceUntilIdle()

        collectJob.cancel()
    }

    @Test
    fun `Login Event with invalid password should emit ShowError side effect`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        viewModel.handleEvent(LoginEvent.EmailChanged(email = "some@email.com"))
        viewModel.handleEvent(LoginEvent.PasswordChanged(password = "1234567"))

        val collectJob =
            launch {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(
                        LoginSideEffect.ShowError(
                            message = "Password must be at least 8 characters long",
                        ),
                    )
                }
            }

        viewModel.handleEvent(LoginEvent.Login)
        advanceUntilIdle()

        collectJob.cancel()
    }

    @Test
    fun `failed login should emit ShowError side effect`() = runTest {
        val viewModel = LoginViewModel(TestCompanyRepository(shouldReturnError = true))

        viewModel.handleEvent(LoginEvent.EmailChanged(email = "some@email.com"))
        viewModel.handleEvent(LoginEvent.PasswordChanged(password = "somePassword"))

        val collectJob =
            launch {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(
                        LoginSideEffect.ShowError(
                            message = "Email or password is incorrect",
                        ),
                    )
                }
            }

        viewModel.handleEvent(LoginEvent.Login)
        advanceUntilIdle()

        collectJob.cancel()
    }

    @Test
    fun `successfully login should emit NavigateToHome side effect`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        viewModel.handleEvent(LoginEvent.EmailChanged(email = "some@email.com"))
        viewModel.handleEvent(LoginEvent.PasswordChanged(password = "somePassword"))

        val collectJob =
            launch {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(LoginSideEffect.NavigateToHome)
                }
            }

        viewModel.handleEvent(LoginEvent.Login)
        advanceUntilIdle()

        collectJob.cancel()
    }
}
