package com.bruno13palhano.mais1venda

import com.bruno13palhano.mais1venda.repository.TestCompanyRepository
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginEvent
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginSideEffect
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.viewmodel.LoginViewModel
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.StandardTestDispatcher
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

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(LoginEvent.EmailChanged(email = expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.email).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `PasswordChanged Event should update password`() = runTest {
        val viewModel = LoginViewModel(TestCompanyRepository())
        val expected = "somePassword"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(
                    LoginEvent.PasswordChanged(password = expected),
                )
            },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.password).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `TogglePasswordVisibility Event should toggle passwordVisibility`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(LoginEvent.TogglePasswordVisibility) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.passwordVisibility).isEqualTo(true)
            },
        )
    }

    @Test
    fun `DismissKeyboard Event should emit DismissKeyboard side effect`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(LoginSideEffect.DismissKeyboard)
                }
            },
            eventsBlock = { viewModel.handleEvent(LoginEvent.DismissKeyboard) },
        )
    }

    @Test
    fun `ForgotPassword Event should emit NavigateToForgotPassword side effect`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(LoginSideEffect.NavigateToForgotPassword)
                }
            },
            eventsBlock = { viewModel.handleEvent(LoginEvent.ForgotPassword) },
        )
    }

    @Test
    fun `CreateAccount Event should emit NavigateToCreateAccount side effect`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(LoginSideEffect.NavigateToCreateAccount)
                }
            },
            eventsBlock = { viewModel.handleEvent(LoginEvent.CreateAccount) },
        )
    }

    @Test
    fun `Login Event with invalid email should set emailError to true`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(LoginEvent.EmailChanged(email = "someemail.com"))
                viewModel.handleEvent(LoginEvent.PasswordChanged(password = "somePassword"))
                viewModel.handleEvent(LoginEvent.Login)
            },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.emailError).isEqualTo(true)
            },
        )
    }

    @Test
    fun `Login Event with invalid password should set passwordError to true`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(LoginEvent.EmailChanged(email = "some@email.com"))
                viewModel.handleEvent(LoginEvent.PasswordChanged(password = "1234567"))
                viewModel.handleEvent(LoginEvent.Login)
            },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.passwordError).isEqualTo(true)
            },
        )
    }

    @Test
    fun `Login Event with invalid email should emit ShowError side effect`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(
                        LoginSideEffect.ShowError(codeError = CodeError.INVALID_EMAIL),
                    )
                }
            },
            eventsBlock = {
                viewModel.handleEvent(LoginEvent.EmailChanged(email = "someemail.com"))
                viewModel.handleEvent(LoginEvent.PasswordChanged(password = "somePassword"))
                viewModel.handleEvent(LoginEvent.Login)
            },
        )
    }

    @Test
    fun `Login Event with invalid password should emit ShowError side effect`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(
                        LoginSideEffect.ShowError(codeError = CodeError.INVALID_PASSWORD),
                    )
                }
            },
            eventsBlock = {
                viewModel.handleEvent(LoginEvent.EmailChanged(email = "some@email.com"))
                viewModel.handleEvent(LoginEvent.PasswordChanged(password = "1234567"))
                viewModel.handleEvent(LoginEvent.Login)
            },
        )
    }

    @Test
    fun `failed login should emit ShowError side effect`() = runTest {
        val viewModel = LoginViewModel(TestCompanyRepository(shouldReturnError = true))

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(
                        LoginSideEffect.ShowError(codeError = CodeError.INVALID_CREDENTIALS),
                    )
                }
            },
            eventsBlock = {
                viewModel.handleEvent(LoginEvent.EmailChanged(email = "some@email.com"))
                viewModel.handleEvent(LoginEvent.PasswordChanged(password = "somePassword"))
                viewModel.handleEvent(LoginEvent.Login)
            },
        )
    }

    @Test
    fun `successfully login should emit NavigateToHome side effect`() = runTest {
        val viewModel = LoginViewModel(companyRepository)

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(LoginSideEffect.NavigateToHome)
                }
            },
            eventsBlock = {
                viewModel.handleEvent(LoginEvent.EmailChanged(email = "some@email.com"))
                viewModel.handleEvent(LoginEvent.PasswordChanged(password = "somePassword"))
                viewModel.handleEvent(LoginEvent.Login)
            },
        )
    }
}
