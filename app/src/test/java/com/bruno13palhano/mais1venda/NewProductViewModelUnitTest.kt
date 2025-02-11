package com.bruno13palhano.mais1venda

import com.bruno13palhano.mais1venda.repository.TestProductRepository
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.NewProductEvent
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.NewProductSideEffect
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.NewProductState
import com.bruno13palhano.mais1venda.ui.screens.products.viewmodel.NewProductViewModel
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
internal class NewProductViewModelUnitTest {
    private val state = NewProductState(
        id = 1L,
        name = "Product 1",
        price = "100.05",
        category = "Category 1",
        description = "Description 1",
        code = "1234567890123",
        quantity = "1",
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `NameChanged Event should update name`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())
        val expected = "Product 2"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(NewProductEvent.NameChanged(expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.name).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `PriceChanged Event should update price`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())
        val expected = "100.05"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(NewProductEvent.PriceChanged(expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.price).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `CategoryChanged Event should update category`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())
        val expected = "Category 2"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(NewProductEvent.CategoryChanged(expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.category).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `DescriptionChanged Event should update description`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())
        val expected = "Description 2"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(NewProductEvent.DescriptionChanged(expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.description).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `CodeChanged Event should update code`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())
        val expected = "1234567890124"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(NewProductEvent.CodeChanged(expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.code).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `QuantityChanged Event should update quantity`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())
        val expected = "2"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(NewProductEvent.QuantityChanged(expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.quantity).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `ToggleExhibitToCatalog Event should toggle exhibitToCatalog`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())
        val expected = true

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(NewProductEvent.ToggleExhibitToCatalog) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.exhibitToCatalog).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `NameChanged with invalid email should set nameError to true`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())
        val expected = true

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(NewProductEvent.NameChanged("")) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.nameError).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `PriceChanged with invalid price should set priceError to true`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())
        val expected = true

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(NewProductEvent.PriceChanged("0.0")) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.priceError).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `CategoryChanged with invalid category should set categoryError to true`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())
        val expected = true

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(NewProductEvent.CategoryChanged("")) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.categoryError).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `DescriptionChanged with invalid description should set descriptionError to true`() =
        runTest {
            val viewModel = NewProductViewModel(state, TestProductRepository())
            val expected = true

            collectStateHelper(
                stateCollector = { viewModel.container.state.collect() },
                eventsBlock = { viewModel.handleEvent(NewProductEvent.DescriptionChanged("")) },
                assertationsBlock = {
                    assertThat(viewModel.container.state.value.descriptionError).isEqualTo(expected)
                },
            )
        }

    @Test
    fun `CodeChanged with invalid code should set codeError to true`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())
        val expected = true

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(NewProductEvent.CodeChanged("")) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.codeError).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `QuantityChanged with invalid quantity should set quantityError to true`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())
        val expected = true

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(NewProductEvent.QuantityChanged("")) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.quantityError).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `DismissKeyboard Event should emit DismissKeyboard side effect`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(NewProductSideEffect.DismissKeyboard)
                }
            },
            eventsBlock = { viewModel.handleEvent(NewProductEvent.DismissKeyboard) },
        )
    }

    @Test
    fun `NavigateBack Event should emit NavigateBack side effect`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(NewProductSideEffect.NavigateBack)
                }
            },
            eventsBlock = { viewModel.handleEvent(NewProductEvent.NavigateBack) },
        )
    }

    @Test
    fun `SaveProduct Event should emit NavigateBack side effect`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(NewProductSideEffect.NavigateBack)
                }
            },
            eventsBlock = {
                viewModel.handleEvent(NewProductEvent.SaveProduct("2022-01-01"))
            },
        )
    }

    @Test
    fun `SaveProduct Event with invalid name should emit ShowError INVALID_FIELDS`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(
                        it,
                    ).isEqualTo(NewProductSideEffect.ShowError(CodeError.INVALID_FIELDS))
                }
            },
            eventsBlock = {
                viewModel.handleEvent(NewProductEvent.NameChanged(""))
                viewModel.handleEvent(NewProductEvent.SaveProduct("2022-01-01"))
            },
        )
    }

    @Test
    fun `SaveProduct Event with invalid price should emit ShowError INVALID_FIELDS`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(
                        it,
                    ).isEqualTo(NewProductSideEffect.ShowError(CodeError.INVALID_FIELDS))
                }
            },
            eventsBlock = {
                viewModel.handleEvent(NewProductEvent.PriceChanged(""))
                viewModel.handleEvent(NewProductEvent.SaveProduct("2022-01-01"))
            },
        )
    }

    @Test
    fun `SaveProduct Event with invalid category should emit ShowError INVALID_FIELDS`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(
                        it,
                    ).isEqualTo(NewProductSideEffect.ShowError(CodeError.INVALID_FIELDS))
                }
            },
            eventsBlock = {
                viewModel.handleEvent(NewProductEvent.CategoryChanged(""))
                viewModel.handleEvent(NewProductEvent.SaveProduct("2022-01-01"))
            },
        )
    }

    @Test
    fun `SaveProduct Event with invalid description should emit ShowError INVALID_FIELDS`() =
        runTest {
            val viewModel = NewProductViewModel(state, TestProductRepository())

            collectEffectHelper(
                verifyEffects = {
                    viewModel.container.sideEffect.collect {
                        assertThat(
                            it,
                        ).isEqualTo(NewProductSideEffect.ShowError(CodeError.INVALID_FIELDS))
                    }
                },
                eventsBlock = {
                    viewModel.handleEvent(NewProductEvent.DescriptionChanged(""))
                    viewModel.handleEvent(NewProductEvent.SaveProduct("2022-01-01"))
                },
            )
        }

    @Test
    fun `SaveProduct Event with invalid code should emit ShowError INVALID_FIELDS`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(
                        it,
                    ).isEqualTo(NewProductSideEffect.ShowError(CodeError.INVALID_FIELDS))
                }
            },
            eventsBlock = {
                viewModel.handleEvent(NewProductEvent.CodeChanged(""))
                viewModel.handleEvent(NewProductEvent.SaveProduct("2022-01-01"))
            },
        )
    }

    @Test
    fun `SaveProduct Event with invalid quantity should emit ShowError INVALID_FIELDS`() = runTest {
        val viewModel = NewProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(
                        it,
                    ).isEqualTo(NewProductSideEffect.ShowError(CodeError.INVALID_FIELDS))
                }
            },
            eventsBlock = {
                viewModel.handleEvent(NewProductEvent.QuantityChanged(""))
                viewModel.handleEvent(NewProductEvent.SaveProduct("2022-01-01"))
            },
        )
    }

    @Test
    fun `SaveProduct Event with invalid param should emit ShowError INVALID_FIELDS`() = runTest {
        val viewModel = NewProductViewModel(NewProductState(), TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(
                        it,
                    ).isEqualTo(NewProductSideEffect.ShowError(CodeError.INVALID_FIELDS))
                }
            },
            eventsBlock = {
                viewModel.handleEvent(NewProductEvent.SaveProduct("2022-01-01"))
            },
        )
    }
}
