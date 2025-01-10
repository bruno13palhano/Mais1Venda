package com.bruno13palhano.mais1venda

import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountEvent
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountSideEffect
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.viewmodel.CreateAccountViewModel
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
internal class CreateAccountViewModelTest {
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `EmailChanged Event should update email property in the State with the provided email`() =
        runTest {
            val viewModel = CreateAccountViewModel()

            val expected = "some@email"

            val collectJob =
                launch {
                    viewModel.container.state.collect()
                }

            viewModel.handleEvent(CreateAccountEvent.EmailChanged(expected))
            advanceUntilIdle()

            assertThat(viewModel.container.state.value.email).isEqualTo(expected)

            collectJob.cancel()
        }

    @Test
    fun `CompanyNameChanged Event should update companyName property in the State with the provided companyName`() =
        runTest {
            val viewModel = CreateAccountViewModel()

            val expected = "company"

            val collectJob =
                launch {
                    viewModel.container.state.collect()
                }

            viewModel.handleEvent(CreateAccountEvent.CompanyNameChanged(expected))
            advanceUntilIdle()

            assertThat(viewModel.container.state.value.companyName).isEqualTo(expected)

            collectJob.cancel()
        }

    @Test
    fun `PhoneChanged Event should update phone property in the State with the provided phone`() =
        runTest {
            val viewModel = CreateAccountViewModel()

            val expected = "1234567"

            val collectJob =
                launch {
                    viewModel.container.state.collect()
                }

            viewModel.handleEvent(CreateAccountEvent.PhoneChanged(expected))

            advanceUntilIdle()

            assertThat(viewModel.container.state.value.phone).isEqualTo(expected)

            collectJob.cancel()
        }

    @Test
    fun `AddressChanged Event should update address property in the State with the provided address`() =
        runTest {
            val viewModel = CreateAccountViewModel()

            val expected = "someAddress"

            val collectJob =
                launch {
                    viewModel.container.state.collect()
                }

            viewModel.handleEvent(CreateAccountEvent.AddressChanged(expected))
            advanceUntilIdle()

            assertThat(viewModel.container.state.value.address).isEqualTo(expected)

            collectJob.cancel()
        }

    @Test
    fun `PasswordChanged Event should update password property in the State with the provided password`() =
        runTest {
            val viewModel = CreateAccountViewModel()

            val expected = "somePassword"

            val collectJob =
                launch {
                    viewModel.container.state.collect()
                }

            viewModel.handleEvent(CreateAccountEvent.PasswordChanged(expected))
            advanceUntilIdle()

            assertThat(viewModel.container.state.value.password).isEqualTo(expected)

            collectJob.cancel()
        }

    @Test
    fun `ConfirmPasswordChanged Event should update confirmPassword property in the State with the provided confirmPassword`() =
        runTest {
            val viewModel = CreateAccountViewModel()

            val expected = "somePassword"

            val collectJob =
                launch {
                    viewModel.container.state.collect()
                }

            viewModel.handleEvent(CreateAccountEvent.ConfirmPasswordChanged(expected))
            advanceUntilIdle()

            assertThat(viewModel.container.state.value.confirmPassword).isEqualTo(expected)

            collectJob.cancel()
        }

    @Test
    fun `DismissKeyboard Event should emit DismissKeyboard side effect`() =
        runTest {
            val viewModel = CreateAccountViewModel()

            val collectJob =
                launch {
                    viewModel.container.sideEffect.collect {
                        assertThat(it).isEqualTo(CreateAccountSideEffect.DismissKeyboard)
                    }
                }

            viewModel.handleEvent(CreateAccountEvent.DismissKeyboard)
            advanceUntilIdle()

            collectJob.cancel()
        }

    @Test
    fun `NavigateToLogin Event should emit NavigateToLogin side effect`() =
        runTest {
            val viewModel = CreateAccountViewModel()

            val collectJob =
                launch {
                    viewModel.container.sideEffect.collect {
                        assertThat(it).isEqualTo(CreateAccountSideEffect.NavigateBack)
                    }
                }

            viewModel.handleEvent(CreateAccountEvent.NavigateBack)
            advanceUntilIdle()

            collectJob.cancel()
        }

    @Test
    fun `CreateAccount Event should emit NavigateToHome side effect`() =
        runTest {
            val viewModel = CreateAccountViewModel()

            val collectJob =
                launch {
                    viewModel.container.sideEffect.collect {
                        assertThat(it).isEqualTo(CreateAccountSideEffect.NavigateToHome)
                    }
                }

            viewModel.handleEvent(CreateAccountEvent.CreateAccount)
            advanceUntilIdle()

            collectJob.cancel()
        }
}
