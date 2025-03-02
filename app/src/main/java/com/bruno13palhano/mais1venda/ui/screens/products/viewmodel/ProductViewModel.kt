package com.bruno13palhano.mais1venda.ui.screens.products.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.ProductEvent
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.ProductMenuItems
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.ProductSideEffect
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.ProductState
import com.bruno13palhano.mais1venda.ui.screens.products.shared.isCodeValid
import com.bruno13palhano.mais1venda.ui.screens.products.shared.isQuantityValid
import com.bruno13palhano.mais1venda.ui.screens.shared.stringToInt
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ProductViewModel @Inject constructor(
    initialState: ProductState,
    private val productRepository: ProductRepository,
) : ViewModel() {
    val container = Container<ProductState, ProductSideEffect>(
        initialState = initialState,
        scope = viewModelScope,
    )

    fun handleEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.GetProduct -> getProduct(id = event.id)

            is ProductEvent.NameChanged -> nameChanged(name = event.name)

            is ProductEvent.CategoryChanged -> categoryChanged(category = event.category)

            is ProductEvent.DescriptionChanged -> descriptionChanged(
                description = event.description,
            )

            is ProductEvent.CodeChanged -> codeChanged(code = event.code)

            is ProductEvent.QuantityChanged -> quantityChanged(quantity = event.quantity)

            ProductEvent.ToggleExhibitToCatalog -> toggleExhibitToCatalog()

            ProductEvent.ToggleOptionsMenu -> toggleOptionsMenu()

            is ProductEvent.UpdateSelectedOption -> updateSelectedOption(option = event.option)

            ProductEvent.DismissKeyboard -> dismissKeyboard()

            is ProductEvent.SaveProduct -> saveProduct(currentDate = event.timestamp, id = event.id)

            ProductEvent.NavigateBack -> navigateBack()
        }
    }

    private fun getProduct(id: Long) = container.intent {
        val product = productRepository.get(id = id)

        if (product != null) {
            reduce { fillProductFields(product = product) }
        } else {
            reduce { copy(isError = true) }
            postSideEffect(
                effect = ProductSideEffect.ShowError(codeError = CodeError.UNKNOWN_ERROR),
            )
        }
    }

    private fun nameChanged(name: String) = container.intent {
        val nameError = name.isBlank()

        reduce { copy(name = name, nameError = nameError) }
    }

    private fun categoryChanged(category: String) = container.intent {
        val categoryError = category.isBlank()

        reduce { copy(category = category, categoryError = categoryError) }
    }

    private fun descriptionChanged(description: String) = container.intent {
        val descriptionError = description.isBlank()

        reduce { copy(description = description, descriptionError = descriptionError) }
    }

    private fun codeChanged(code: String) = container.intent {
        val codeError = !isCodeValid(code = code)

        reduce { copy(code = code, codeError = codeError) }
    }

    private fun quantityChanged(quantity: String) = container.intent {
        val quantityError = !isQuantityValid(quantity = stringToInt(quantity))

        reduce { copy(quantity = quantity, quantityError = quantityError) }
    }

    private fun toggleExhibitToCatalog() = container.intent {
        reduce { copy(exhibitToCatalog = !exhibitToCatalog) }
    }

    private fun toggleOptionsMenu() = container.intent {
        reduce { copy(openOptionsMenu = !openOptionsMenu) }
    }

    private fun updateSelectedOption(option: ProductMenuItems) = container.intent {
        when (option) {
            ProductMenuItems.DELETE -> {
                container.state.value.id?.let {
                    if (productRepository.delete(id = it)) {
                        navigateBack()
                    } else {
                        reduce { copy(isError = true) }
                        postSideEffect(
                            effect = ProductSideEffect.ShowError(
                                codeError = CodeError.UNKNOWN_ERROR,
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun dismissKeyboard() = container.intent {
        postSideEffect(effect = ProductSideEffect.DismissKeyboard)
    }

    private fun navigateBack() = container.intent {
        postSideEffect(effect = ProductSideEffect.NavigateBack)
    }

    private fun saveProduct(currentDate: String, id: Long = 0L) = container.intent {
        if (isFieldsInvalid()) {
            reduce { copy(isError = true) }
            postSideEffect(
                effect = ProductSideEffect.ShowError(
                    codeError = CodeError.INVALID_FIELDS,
                ),
            )

            return@intent
        }

        val result = if (id == 0L) {
            productRepository.insert(
                product = Product(
                    id = 0L,
                    name = container.state.value.name,
                    image = byteArrayOf(),
                    category = listOf(),
                    description = container.state.value.description,
                    code = container.state.value.code,
                    quantity = stringToInt(container.state.value.quantity),
                    exhibitToCatalog = container.state.value.exhibitToCatalog,
                    lastModifiedTimestamp = currentDate,
                ),
            )
        } else {
            productRepository.update(
                product = Product(
                    id = id,
                    name = container.state.value.name,
                    image = byteArrayOf(),
                    category = listOf(),
                    description = container.state.value.description,
                    code = container.state.value.code,
                    quantity = stringToInt(container.state.value.quantity),
                    exhibitToCatalog = container.state.value.exhibitToCatalog,
                    lastModifiedTimestamp = currentDate,
                ),
            )
        }

        if (result) {
            postSideEffect(effect = ProductSideEffect.NavigateBack)
        } else {
            reduce { copy(isError = true) }
            postSideEffect(
                effect = ProductSideEffect.ShowError(
                    codeError = CodeError.UNKNOWN_ERROR,
                ),
            )
        }
    }

    private fun isFieldsInvalid(): Boolean {
        val nameError = container.state.value.name.isBlank()
        val categoryError = container.state.value.category.isBlank()
        val descriptionError = container.state.value.description.isBlank()
        val codeError = !isCodeValid(code = container.state.value.code)
        val quantityError = !isQuantityValid(quantity = stringToInt(container.state.value.quantity))

        return nameError || categoryError || descriptionError || codeError || quantityError
    }

    private fun fillProductFields(product: Product): ProductState {
        return ProductState(
            id = product.id,
            name = product.name,
            category = product.category.toString(),
            description = product.description,
            code = product.code,
            quantity = product.quantity.toString(),
            exhibitToCatalog = product.exhibitToCatalog,
        )
    }
}
