package com.bruno13palhano.mais1venda

import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.mais1venda.repository.TestProductRepository
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.ProductEvent
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.ProductMenuItems
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.ProductSideEffect
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.ProductState
import com.bruno13palhano.mais1venda.ui.screens.products.viewmodel.ProductViewModel
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
internal class ProductViewModelUnitTest {
    private val state = ProductState(
        id = 1L,
        name = "Product 1",
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
    fun `GetProduct Event should set input fields`() = runTest {
        val repository = TestProductRepository()
        val viewModel = ProductViewModel(state, repository)
        val product = Product(
            id = 2L,
            name = "Product 2",
            category = listOf(),
            description = "Description 2",
            code = "1234567890123",
            quantity = 1,
            exhibitToCatalog = true,
            lastModifiedTimestamp = "2025-02-13",
        )

        repository.insert(product = product)
        repository.insert(product = product.copy(id = 3L, name = "Product 3"))

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = {
                viewModel.handleEvent(ProductEvent.GetProduct(2L))
            },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.id).isEqualTo(2L)
                assertThat(viewModel.container.state.value.name).isEqualTo("Product 2")
            },
        )
    }

    @Test
    fun `NameChanged Event should update name`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())
        val expected = "Product 2"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(ProductEvent.NameChanged(expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.name).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `CategoryChanged Event should update category`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())
        val expected = "Category 2"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(ProductEvent.CategoryChanged(expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.category).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `DescriptionChanged Event should update description`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())
        val expected = "Description 2"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(ProductEvent.DescriptionChanged(expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.description).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `CodeChanged Event should update code`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())
        val expected = "1234567890124"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(ProductEvent.CodeChanged(expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.code).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `QuantityChanged Event should update quantity`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())
        val expected = "2"

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(ProductEvent.QuantityChanged(expected)) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.quantity).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `ToggleExhibitToCatalog Event should toggle exhibitToCatalog`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())
        val expected = true

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(ProductEvent.ToggleExhibitToCatalog) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.exhibitToCatalog).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `NameChanged with invalid email should set nameError to true`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())
        val expected = true

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(ProductEvent.NameChanged("")) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.nameError).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `CategoryChanged with invalid category should set categoryError to true`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())
        val expected = true

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(ProductEvent.CategoryChanged("")) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.categoryError).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `DescriptionChanged with invalid description should set descriptionError to true`() =
        runTest {
            val viewModel = ProductViewModel(state, TestProductRepository())
            val expected = true

            collectStateHelper(
                stateCollector = { viewModel.container.state.collect() },
                eventsBlock = { viewModel.handleEvent(ProductEvent.DescriptionChanged("")) },
                assertationsBlock = {
                    assertThat(viewModel.container.state.value.descriptionError).isEqualTo(expected)
                },
            )
        }

    @Test
    fun `CodeChanged with invalid code should set codeError to true`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())
        val expected = true

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(ProductEvent.CodeChanged("")) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.codeError).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `QuantityChanged with invalid quantity should set quantityError to true`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())
        val expected = true

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(ProductEvent.QuantityChanged("")) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.quantityError).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `ToggleOptionsMenu Event should toggle openOptionsMenu`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())
        val expected = true

        collectStateHelper(
            stateCollector = { viewModel.container.state.collect() },
            eventsBlock = { viewModel.handleEvent(ProductEvent.ToggleOptionsMenu) },
            assertationsBlock = {
                assertThat(viewModel.container.state.value.openOptionsMenu).isEqualTo(expected)
            },
        )
    }

    @Test
    fun `delete product with UpdateSelectedOption Event should emit NavigateBack side effect`() =
        runTest {
            val viewModel = ProductViewModel(state, TestProductRepository())
            val expected = ProductMenuItems.DELETE

            collectEffectHelper(
                verifyEffects = {
                    viewModel.container.sideEffect.collect {
                        assertThat(it).isEqualTo(ProductSideEffect.NavigateBack)
                    }
                },
                eventsBlock = {
                    viewModel.handleEvent(ProductEvent.UpdateSelectedOption(expected))
                },
            )
        }

    @Test
    fun `failure delete with UpdateSelectedOption Event should emit ShowError side effect`() =
        runTest {
            val viewModel = ProductViewModel(state, TestProductRepository(shouldReturnError = true))
            val expected = ProductMenuItems.DELETE

            collectEffectHelper(
                verifyEffects = {
                    viewModel.container.sideEffect.collect {
                        assertThat(
                            it,
                        ).isEqualTo(ProductSideEffect.ShowError(CodeError.UNKNOWN_ERROR))
                    }
                },
                eventsBlock = {
                    viewModel.handleEvent(ProductEvent.UpdateSelectedOption(expected))
                },
            )
        }

    @Test
    fun `DismissKeyboard Event should emit DismissKeyboard side effect`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(ProductSideEffect.DismissKeyboard)
                }
            },
            eventsBlock = { viewModel.handleEvent(ProductEvent.DismissKeyboard) },
        )
    }

    @Test
    fun `NavigateBack Event should emit NavigateBack side effect`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(ProductSideEffect.NavigateBack)
                }
            },
            eventsBlock = { viewModel.handleEvent(ProductEvent.NavigateBack) },
        )
    }

    @Test
    fun `new products with SaveProduct Event should emit NavigateBack side effect`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(ProductSideEffect.NavigateBack)
                }
            },
            eventsBlock = {
                viewModel.handleEvent(ProductEvent.SaveProduct("2022-01-01", 0L))
            },
        )
    }

    @Test
    fun `edit product with SaveProduct Event should emit NavigateBack side effect`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(it).isEqualTo(ProductSideEffect.NavigateBack)
                }
            },
            eventsBlock = {
                viewModel.handleEvent(ProductEvent.SaveProduct(timestamp = "2022-01-01", id = 1L))
            },
        )
    }

    @Test
    fun `SaveProduct Event with invalid name should emit ShowError INVALID_FIELDS`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(
                        it,
                    ).isEqualTo(ProductSideEffect.ShowError(CodeError.INVALID_FIELDS))
                }
            },
            eventsBlock = {
                viewModel.handleEvent(ProductEvent.NameChanged(""))
                viewModel.handleEvent(ProductEvent.SaveProduct("2022-01-01", 0L))
            },
        )
    }

    @Test
    fun `SaveProduct Event with invalid category should emit ShowError INVALID_FIELDS`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(
                        it,
                    ).isEqualTo(ProductSideEffect.ShowError(CodeError.INVALID_FIELDS))
                }
            },
            eventsBlock = {
                viewModel.handleEvent(ProductEvent.CategoryChanged(""))
                viewModel.handleEvent(ProductEvent.SaveProduct("2022-01-01", 0L))
            },
        )
    }

    @Test
    fun `SaveProduct Event with invalid description should emit ShowError INVALID_FIELDS`() =
        runTest {
            val viewModel = ProductViewModel(state, TestProductRepository())

            collectEffectHelper(
                verifyEffects = {
                    viewModel.container.sideEffect.collect {
                        assertThat(
                            it,
                        ).isEqualTo(ProductSideEffect.ShowError(CodeError.INVALID_FIELDS))
                    }
                },
                eventsBlock = {
                    viewModel.handleEvent(ProductEvent.DescriptionChanged(""))
                    viewModel.handleEvent(ProductEvent.SaveProduct("2022-01-01", 0L))
                },
            )
        }

    @Test
    fun `SaveProduct Event with invalid code should emit ShowError INVALID_FIELDS`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(
                        it,
                    ).isEqualTo(ProductSideEffect.ShowError(CodeError.INVALID_FIELDS))
                }
            },
            eventsBlock = {
                viewModel.handleEvent(ProductEvent.CodeChanged(""))
                viewModel.handleEvent(ProductEvent.SaveProduct("2022-01-01", 0L))
            },
        )
    }

    @Test
    fun `SaveProduct Event with invalid quantity should emit ShowError INVALID_FIELDS`() = runTest {
        val viewModel = ProductViewModel(state, TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(
                        it,
                    ).isEqualTo(ProductSideEffect.ShowError(CodeError.INVALID_FIELDS))
                }
            },
            eventsBlock = {
                viewModel.handleEvent(ProductEvent.QuantityChanged(""))
                viewModel.handleEvent(ProductEvent.SaveProduct("2022-01-01", 0L))
            },
        )
    }

    @Test
    fun `SaveProduct Event with invalid param should emit ShowError INVALID_FIELDS`() = runTest {
        val viewModel = ProductViewModel(ProductState(), TestProductRepository())

        collectEffectHelper(
            verifyEffects = {
                viewModel.container.sideEffect.collect {
                    assertThat(
                        it,
                    ).isEqualTo(ProductSideEffect.ShowError(CodeError.INVALID_FIELDS))
                }
            },
            eventsBlock = {
                viewModel.handleEvent(ProductEvent.SaveProduct("2022-01-01", 0L))
            },
        )
    }
}
