package com.bruno13palhano.mais1venda

import com.bruno13palhano.mais1venda.repository.TestCompanyRepository
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountEvent
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountSideEffect
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountState
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.viewmodel.CreateAccountViewModel
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
internal class CreateAccountViewModelTest {
    private val state =
        CreateAccountState(
            email = "some@email.com",
            companyName = "company",
            phone = "12345678910",
            password = "12345678",
            confirmPassword = "12345678",
            address = "someAddress",
        )

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `EmailChanged Event should update email`() = runTest {
        val viewModel = CreateAccountViewModel(
            initialState = CreateAccountState(),
            companyRepository = TestCompanyRepository(),
        )
        val expected = "some@email"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(CreateAccountEvent.EmailChanged(expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.email).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `CompanyNameChanged Event should update companyName`() = runTest {
        val viewModel = CreateAccountViewModel(
            initialState = CreateAccountState(),
            companyRepository = TestCompanyRepository(),
        )
        val expected = "company"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(CreateAccountEvent.CompanyNameChanged(expected))
            },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.companyName).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `PhoneChanged Event should update phone`() = runTest {
        val viewModel = CreateAccountViewModel(
            initialState = CreateAccountState(),
            companyRepository = TestCompanyRepository(),
        )
        val expected = "1234567"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(CreateAccountEvent.PhoneChanged(expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.phone).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `AddressChanged Event should update address`() = runTest {
        val viewModel = CreateAccountViewModel(
            initialState = CreateAccountState(),
            companyRepository = TestCompanyRepository(),
        )
        val expected = "someAddress"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(CreateAccountEvent.AddressChanged(expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.address).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `PasswordChanged Event should update password`() = runTest {
        val viewModel = CreateAccountViewModel(
            initialState = CreateAccountState(),
            companyRepository = TestCompanyRepository(),
        )
        val expected = "somePassword"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(CreateAccountEvent.PasswordChanged(expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.password).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `ConfirmPasswordChanged Event should update confirmPassword`() = runTest {
        val viewModel = CreateAccountViewModel(
            initialState = CreateAccountState(),
            companyRepository = TestCompanyRepository(),
        )
        val expected = "somePassword"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(CreateAccountEvent.ConfirmPasswordChanged(expected))
            },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.confirmPassword).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `DismissKeyboard Event should emit DismissKeyboard side effect`() = runTest {
        val viewModel = CreateAccountViewModel(
            initialState = CreateAccountState(),
            companyRepository = TestCompanyRepository(),
        )

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(CreateAccountSideEffect.DismissKeyboard)
                }
            },
            eventsBlock = { viewModel.handleEvent(CreateAccountEvent.DismissKeyboard) },
        )
    }

    @Test
    fun `NavigateToLogin Event should emit NavigateToLogin side effect`() = runTest {
        val viewModel = CreateAccountViewModel(
            initialState = CreateAccountState(),
            companyRepository = TestCompanyRepository(),
        )

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(CreateAccountSideEffect.NavigateBack)
                }
            },
            eventsBlock = { viewModel.handleEvent(CreateAccountEvent.NavigateBack) },
        )
    }

    @Test
    fun `EmailChanged with invalid email should set emailError to true`() = runTest {
        val expected = state.copy(email = "someemail.com", emailError = true)
        val viewModel = CreateAccountViewModel(
            initialState = state,
            companyRepository = TestCompanyRepository(),
        )

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(CreateAccountEvent.EmailChanged(email = expected.email))
            },
            assertationsBlock = {
                assertThat(viewModel.container.state.value).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `CreateAccount with invalid email should emit ShowError INVALID_EMAIL`() = runTest {
        val initialState = state.copy(email = "someemail.com")
        val viewModel = CreateAccountViewModel(
            initialState = initialState,
            companyRepository = TestCompanyRepository(),
        )

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(
                        CreateAccountSideEffect.ShowError(
                            codeError = CodeError.INVALID_EMAIL,
                        ),
                    )
                }
            },
            eventsBlock = { viewModel.handleEvent(CreateAccountEvent.CreateAccount) },
        )
    }

    @Test
    fun `PasswordChanged with invalid password should set passwordError to true`() = runTest {
        val expected = state.copy(
            password = "1234567",
            confirmPassword = "1234567",
            passwordError = true,
        )
        val viewModel = CreateAccountViewModel(
            initialState = state.copy(confirmPassword = "1234567"),
            companyRepository = TestCompanyRepository(),
        )

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(
                    CreateAccountEvent.PasswordChanged(password = expected.password),
                )
            },
            assertationsBlock = {
                assertThat(viewModel.container.state.value).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `CreateAccount with invalid password should emit ShowError INVALID_PASSWORD`() = runTest {
        val initialState = state.copy(password = "1234567", confirmPassword = "1234567")
        val viewModel = CreateAccountViewModel(
            initialState = initialState,
            companyRepository = TestCompanyRepository(),
        )

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(
                        CreateAccountSideEffect.ShowError(codeError = CodeError.INVALID_PASSWORD),
                    )
                }
            },
            eventsBlock = { viewModel.handleEvent(CreateAccountEvent.CreateAccount) },
        )
    }

    @Test
    fun `ConfirmPasswordChanged with mismatch password should set mismatchError to true`() =
        runTest {
            val expected = state.copy(confirmPassword = "1234567", mismatchError = true)
            val viewModel = CreateAccountViewModel(
                initialState = state,
                companyRepository = TestCompanyRepository(),
            )

            collectStateHelper(
                stateCollector = { viewModel.container.state.collect() },
                eventsBlock = {
                    viewModel.handleEvent(
                        CreateAccountEvent.ConfirmPasswordChanged(
                            confirmPassword = expected.confirmPassword,
                        ),
                    )
                },
                assertationsBlock = {
                    assertThat(viewModel.container.state.value).isEqualTo(expected)
                },
            )
        }

    @Test
    fun `CreateAccount with mismatch password should emit ShowError PASSWORD_MISMATCH`() = runTest {
        val initialState = state.copy(confirmPassword = "1234567")
        val viewModel = CreateAccountViewModel(
            initialState = initialState,
            companyRepository = TestCompanyRepository(),
        )

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(
                        CreateAccountSideEffect.ShowError(
                            codeError = CodeError.PASSWORD_MISMATCH,
                        ),
                    )
                }
            },
            eventsBlock = { viewModel.handleEvent(CreateAccountEvent.CreateAccount) },
        )
    }

    @Test
    fun `CompanyNameChanged with invalid companyName should ser companyNameError to true`() =
        runTest {
            val expected = state.copy(companyName = "", companyNameError = true)
            val viewModel = CreateAccountViewModel(
                initialState = state,
                companyRepository = TestCompanyRepository(),
            )

            collectStateHelper(
                stateCollector = { viewModel.container.state.collect() },
                eventsBlock = {
                    viewModel.handleEvent(
                        CreateAccountEvent.CompanyNameChanged(
                            companyName = expected.companyName,
                        ),
                    )
                },
                assertationsBlock = {
                    assertThat(viewModel.container.state.value).isEqualTo(expected)
                },
            )
        }

    @Test
    fun `CreateAccount with invalid companyName should emit ShowError INVALID_COMPANY_NAME`() =
        runTest {
            val initialState = state.copy(companyName = "")
            val viewModel = CreateAccountViewModel(
                initialState = initialState,
                companyRepository = TestCompanyRepository(),
            )

            collectEffectHelper(
                verifyEffects = {
                    viewModel.container.sideEffect.collect {
                        assertThat(it).isEqualTo(
                            CreateAccountSideEffect.ShowError(
                                codeError = CodeError.INVALID_COMPANY_NAME,
                            ),
                        )
                    }
                },
                eventsBlock = { viewModel.handleEvent(CreateAccountEvent.CreateAccount) },
            )
        }

    @Test
    fun `PhoneChanged with invalid phone should ser phoneError to true`() = runTest {
        val expected = state.copy(phone = "1234567891", phoneError = true)
        val viewModel = CreateAccountViewModel(
            initialState = state,
            companyRepository = TestCompanyRepository(),
        )

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(
                    CreateAccountEvent.PhoneChanged(phone = expected.phone),
                )
            },
            assertationsBlock = {
                assertThat(viewModel.container.state.value).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `CreateAccount with invalid phone should emit ShowError INVALID_PHONE`() = runTest {
        val initialState = state.copy(phone = "1234567891")
        val viewModel = CreateAccountViewModel(
            initialState = initialState,
            companyRepository = TestCompanyRepository(),
        )

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(
                        CreateAccountSideEffect.ShowError(
                            codeError = CodeError.INVALID_PHONE,
                        ),
                    )
                }
            },
            eventsBlock = { viewModel.handleEvent(CreateAccountEvent.CreateAccount) },
        )
    }

    @Test
    fun `AddressChanged with invalid address should ser AddressError to true`() = runTest {
        val expected = state.copy(address = "", addressError = true)
        val viewModel = CreateAccountViewModel(
            initialState = state,
            companyRepository = TestCompanyRepository(),
        )

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(
                    CreateAccountEvent.AddressChanged(address = expected.address),
                )
            },
            assertationsBlock = {
                assertThat(viewModel.container.state.value).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `CreateAccount with invalid address should emit ShowError INVALID_ADDRESS`() = runTest {
        val initialState = state.copy(address = "")
        val viewModel = CreateAccountViewModel(
            initialState = initialState,
            companyRepository = TestCompanyRepository(),
        )

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(
                        CreateAccountSideEffect.ShowError(
                            codeError = CodeError.INVALID_ADDRESS,
                        ),
                    )
                }
            },
            eventsBlock = { viewModel.handleEvent(CreateAccountEvent.CreateAccount) },
        )
    }

    @Test
    fun `CreateAccount Event should emit NavigateToHome side effect`() = runTest {
        val viewModel = CreateAccountViewModel(
            initialState = state,
            companyRepository = TestCompanyRepository(),
        )

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(CreateAccountSideEffect.NavigateToHome)
                }
            },
            eventsBlock = { viewModel.handleEvent(CreateAccountEvent.CreateAccount) },
        )
    }

    @Test
    fun `CreateAccount Event with invalid email should set emailError to true`() = runTest {
        val expected = state.copy(email = "someemail.com", emailError = true)
        val viewModel = CreateAccountViewModel(
            initialState = state,
            companyRepository = TestCompanyRepository(),
        )

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(CreateAccountEvent.EmailChanged(email = expected.email))
                viewModel.handleEvent(CreateAccountEvent.CreateAccount)
            },
            assertationsBlock = { assertThat(viewModel.container.state.value).isEqualTo(expected) },
        )
    }

    @Test
    fun `CreateAccount Event with invalid password should set passwordError to true`() = runTest {
        val expected = state.copy(
            password = "1234567",
            confirmPassword = "1234567",
            passwordError = true,
        )
        val viewModel = CreateAccountViewModel(
            initialState = state.copy(confirmPassword = "1234567"),
            companyRepository = TestCompanyRepository(),
        )

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(
                    CreateAccountEvent.PasswordChanged(password = expected.password),
                )
                viewModel.handleEvent(CreateAccountEvent.CreateAccount)
            },
            assertationsBlock = { assertThat(viewModel.container.state.value).isEqualTo(expected) },
        )
    }

    @Test
    fun `CreateAccount Event with mismatch password should set mismatchError to true`() = runTest {
        val expected = state.copy(confirmPassword = "1234567", mismatchError = true)
        val viewModel = CreateAccountViewModel(
            initialState = state,
            companyRepository = TestCompanyRepository(),
        )

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(
                    CreateAccountEvent.ConfirmPasswordChanged(
                        confirmPassword = expected.confirmPassword,
                    ),
                )
                viewModel.handleEvent(CreateAccountEvent.CreateAccount)
            },
            assertationsBlock = {
                assertThat(viewModel.container.state.value).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `CreateAccount Event with invalid companyName should set companyNameError to true`() =
        runTest {
            val expected = state.copy(companyName = "", companyNameError = true)
            val viewModel = CreateAccountViewModel(
                initialState = state,
                companyRepository = TestCompanyRepository(),
            )

            collectStateHelper(
                stateCollector = { viewModel.container.state.collect() },
                eventsBlock = {
                    viewModel.handleEvent(
                        CreateAccountEvent.CompanyNameChanged(companyName = expected.companyName),
                    )
                    viewModel.handleEvent(CreateAccountEvent.CreateAccount)
                },
                assertationsBlock = {
                    assertThat(viewModel.container.state.value).isEqualTo(expected)
                },
            )
        }

    @Test
    fun `CreateAccount Event with invalid phone should set phoneError to true`() = runTest {
        val expected = state.copy(phone = "1234567891", phoneError = true)
        val viewModel = CreateAccountViewModel(
            initialState = state,
            companyRepository = TestCompanyRepository(),
        )

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(CreateAccountEvent.PhoneChanged(phone = expected.phone))
                viewModel.handleEvent(CreateAccountEvent.CreateAccount)
            },
            assertationsBlock = { assertThat(viewModel.container.state.value).isEqualTo(expected) },
        )
    }

    @Test
    fun `CreateAccount Event with invalid address should set addressError to true`() = runTest {
        val expected = state.copy(address = "", addressError = true)
        val viewModel = CreateAccountViewModel(
            initialState = state,
            companyRepository = TestCompanyRepository(),
        )

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(CreateAccountEvent.AddressChanged(address = expected.address))
                viewModel.handleEvent(CreateAccountEvent.CreateAccount)
            },
            assertationsBlock = { assertThat(viewModel.container.state.value).isEqualTo(expected) },
        )
    }

    @Test
    fun `CreateAccount Event with invalid fields should set fieldsError to true`() = runTest {
        val expected = state.copy(
            email = "",
            address = "",
            emailError = true,
            addressError = true,
            fieldsError = true,
        )
        val viewModel = CreateAccountViewModel(
            initialState = state,
            companyRepository = TestCompanyRepository(),
        )

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(CreateAccountEvent.EmailChanged(email = expected.email))
                viewModel.handleEvent(
                    CreateAccountEvent.AddressChanged(address = expected.address),
                )
                viewModel.handleEvent(CreateAccountEvent.CreateAccount)
            },
            assertationsBlock = {
                assertThat(
                    viewModel.container.state.value,
                ).isEqualTo(expected)
            },
        )
    }
}
