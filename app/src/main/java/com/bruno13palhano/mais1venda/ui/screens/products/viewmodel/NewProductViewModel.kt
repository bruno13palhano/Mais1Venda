package com.bruno13palhano.mais1venda.ui.screens.products.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.NewProductEvent
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.NewProductSideEffect
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.NewProductState
import com.bruno13palhano.mais1venda.ui.screens.products.shared.isCodeValid
import com.bruno13palhano.mais1venda.ui.screens.products.shared.isPriceValid
import com.bruno13palhano.mais1venda.ui.screens.products.shared.isQuantityValid
import com.bruno13palhano.mais1venda.ui.screens.shared.currentDate
import com.bruno13palhano.mais1venda.ui.screens.shared.dateFormat
import com.bruno13palhano.mais1venda.ui.screens.shared.stringToFloat
import com.bruno13palhano.mais1venda.ui.screens.shared.stringToInt
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
        val nameError = name.isBlank()

        reduce { copy(name = name, nameError = nameError) }
    }

    private fun priceChanged(price: String) = container.intent {
        val priceError = !isPriceValid(price = stringToFloat(price))

        reduce { copy(price = price, priceError = priceError) }
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

    private fun dismissKeyboard() = container.intent {
        postSideEffect(effect = NewProductSideEffect.DismissKeyboard)
    }

    private fun navigateBack() = container.intent {
        postSideEffect(effect = NewProductSideEffect.NavigateBack)
    }

    private fun saveProduct() = container.intent {
        if (isFieldsInvalid()) {
            reduce { copy(isError = true) }
            postSideEffect(
                effect = NewProductSideEffect.ShowError(
                    codeError = CodeError.UNKNOWN_ERROR
                )
            )

            return@intent
        }

        val response = productRepository.insert(
            product = Product(
                id = 0L,
                name = container.state.value.name,
                price = stringToFloat(container.state.value.price),
                category = listOf(),
                description = container.state.value.description,
                code = container.state.value.code,
                quantity = stringToInt(container.state.value.quantity),
                exhibitToCatalog = container.state.value.exhibitToCatalog,
                lastModifiedTimestamp = dateFormat.format(currentDate()),
            ),
        )

        when (response) {
            is Resource.Success -> {
                if (response.data != null) {
                    postSideEffect(effect = NewProductSideEffect.NavigateBack)
                }
            }

            is Resource.ResponseError -> {
                reduce { copy(isError = true) }
                postSideEffect(
                    effect = NewProductSideEffect.ShowError(
                        codeError = CodeError.UNKNOWN_ERROR
                    )
                )
            }

            is Resource.Error -> {
                reduce { copy(isError = true) }
                postSideEffect(
                    effect = NewProductSideEffect.ShowError(
                        codeError = CodeError.UNKNOWN_ERROR
                    )
                )
            }
        }
    }

    private fun isFieldsInvalid(): Boolean {
        val nameError = container.state.value.name.isBlank()
        val priceError = !isPriceValid(price = stringToFloat(container.state.value.price))
        val categoryError = container.state.value.category.isBlank()
        val descriptionError = container.state.value.description.isBlank()
        val codeError = !isCodeValid(code = container.state.value.code)
        val quantityError = !isQuantityValid(quantity = stringToInt(container.state.value.quantity))

        return nameError || priceError || categoryError || descriptionError || codeError ||
                quantityError
    }
}
