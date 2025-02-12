package com.bruno13palhano.mais1venda.ui.screens.products.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError

@Immutable
internal data class NewProductState(
    val id: Long? = null,
    val name: String = "",
    val price: String = "",
    val category: String = "",
    val description: String = "",
    val code: String = "",
    val quantity: String = "",
    val exhibitToCatalog: Boolean = false,
    val nameError: Boolean = false,
    val priceError: Boolean = false,
    val categoryError: Boolean = false,
    val descriptionError: Boolean = false,
    val codeError: Boolean = false,
    val quantityError: Boolean = false,
    val isError: Boolean = false,
)

@Immutable
internal sealed interface NewProductEvent {
    data class NameChanged(val name: String) : NewProductEvent
    data class PriceChanged(val price: String) : NewProductEvent
    data class CategoryChanged(val category: String) : NewProductEvent
    data class DescriptionChanged(val description: String) : NewProductEvent
    data class CodeChanged(val code: String) : NewProductEvent
    data class QuantityChanged(val quantity: String) : NewProductEvent
    data object ToggleExhibitToCatalog : NewProductEvent
    data object DismissKeyboard : NewProductEvent
    data class SaveProduct(val timestamp: String) : NewProductEvent
    data object NavigateBack : NewProductEvent
}

@Immutable
internal sealed interface NewProductSideEffect {
    data object DismissKeyboard : NewProductSideEffect
    data object NavigateBack : NewProductSideEffect
    data class ShowError(val codeError: CodeError) : NewProductSideEffect
}
