package com.bruno13palhano.mais1venda.ui.screens.products.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError

@Immutable
internal data class ProductState(
    val id: Long? = null,
    val name: String = "",
    val image: ByteArray? = null,
    val category: String = "",
    val description: String = "",
    val code: String = "",
    val quantity: String = "",
    val openOptionsMenu: Boolean = false,
    val exhibitToCatalog: Boolean = false,
    val nameError: Boolean = false,
    val categoryError: Boolean = false,
    val descriptionError: Boolean = false,
    val codeError: Boolean = false,
    val quantityError: Boolean = false,
    val isError: Boolean = false,
)

@Immutable
internal sealed interface ProductEvent {
    data class GetProduct(val id: Long) : ProductEvent
    data class NameChanged(val name: String) : ProductEvent
    data class ImageChanged(val image: ByteArray?) : ProductEvent
    data class CategoryChanged(val category: String) : ProductEvent
    data class DescriptionChanged(val description: String) : ProductEvent
    data class CodeChanged(val code: String) : ProductEvent
    data class QuantityChanged(val quantity: String) : ProductEvent
    data object ToggleExhibitToCatalog : ProductEvent
    data object ToggleOptionsMenu : ProductEvent
    data class UpdateSelectedOption(val option: ProductMenuItems) : ProductEvent
    data object DismissKeyboard : ProductEvent
    data class SaveProduct(val timestamp: String, val id: Long) : ProductEvent
    data object NavigateBack : ProductEvent
}

@Immutable
internal sealed interface ProductSideEffect {
    data object DismissKeyboard : ProductSideEffect
    data object NavigateBack : ProductSideEffect
    data class ShowError(val codeError: CodeError) : ProductSideEffect
}

internal enum class ProductMenuItems {
    DELETE,
}
