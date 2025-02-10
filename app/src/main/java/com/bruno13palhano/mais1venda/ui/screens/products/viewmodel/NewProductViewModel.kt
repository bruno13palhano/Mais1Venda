package com.bruno13palhano.mais1venda.ui.screens.products.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.NewProductEvent
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.NewProductSideEffect
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.NewProductState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class NewProductViewModel @Inject constructor(
    initialState: NewProductState,
    private val productRepository: ProductRepository,
) : ViewModel() {
    val container = Container<NewProductState, NewProductSideEffect>(
        initialState = initialState,
        scope = viewModelScope,
    )

    fun handleEvent(event: NewProductEvent) {
        when (event) {
            is NewProductEvent.NameChanged -> nameChanged(name = event.name)

            is NewProductEvent.PriceChanged -> priceChanged(price = event.price)

            is NewProductEvent.CategoryChanged -> categoryChanged(category = event.category)

            is NewProductEvent.DescriptionChanged -> descriptionChanged(
                description = event.description,
            )

            is NewProductEvent.CodeChanged -> codeChanged(code = event.code)

            is NewProductEvent.QuantityChanged -> quantityChanged(quantity = event.quantity)

            NewProductEvent.ToggleExhibitToCatalog -> toggleExhibitToCatalog()

            NewProductEvent.DismissKeyboard -> dismissKeyboard()

            NewProductEvent.SaveProduct -> saveProduct()

            NewProductEvent.NavigateBack -> navigateBack()
        }
    }

    private fun nameChanged(name: String) = container.intent {
        var nameError = false

        if (name.isBlank()) nameError = true

        reduce { copy(name = name, nameError = nameError) }
    }

    private fun priceChanged(price: String) = container.intent {
        var priceError = false

        if (price.isBlank()) priceError = true

        reduce { copy(price = price, priceError = priceError) }
    }

    private fun categoryChanged(category: String) = container.intent {
        var categoryError = false

        if (category.isBlank()) categoryError = true

        reduce { copy(category = category, categoryError = categoryError) }
    }

    private fun descriptionChanged(description: String) = container.intent {
        var descriptionError = false

        if (description.isBlank()) descriptionError = true

        reduce { copy(description = description, descriptionError = descriptionError) }
    }

    private fun codeChanged(code: String) = container.intent {
        var codeError = false

        if (code.isBlank()) codeError = true

        reduce { copy(code = code, codeError = codeError) }
    }

    private fun quantityChanged(quantity: String) = container.intent {
        var quantityError = false

        if (quantity.isBlank() && quantity.toInt() <= 0) quantityError = true

        reduce { copy(quantity = quantity, quantityError = quantityError) }
    }

    private fun toggleExhibitToCatalog() = container.intent {
        reduce { copy(exhibitToCatalog = !exhibitToCatalog) }
    }

    private fun dismissKeyboard() = container.intent {
        postSideEffect(effect = NewProductSideEffect.DismissKeyboard)
    }

    private fun saveProduct() = container.intent {
    }

    private fun navigateBack() = container.intent {
        postSideEffect(effect = NewProductSideEffect.NavigateBack)
    }
}
