package com.bruno13palhano.mais1venda.ui.screens.products.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.data.model.company.Product

@Immutable
internal data class ProductsState(
    val products: List<Product> = emptyList(),
    val selectedOption: String? = null,
)

@Immutable
internal sealed interface ProductsEvent {
    data object LoadProducts : ProductsEvent
    data object OpenOptionsMenu : ProductsEvent
    data class OptionsItemSelected(val option: String) : ProductsEvent
    data class NavigateToProduct(val productId: Long) : ProductsEvent
    data object NavigateToNewProduct : ProductsEvent
    data object NavigateBack : ProductsEvent
}

@Immutable
internal sealed interface ProductsSideEffect {
    data object OpenOptionsMenu : ProductsSideEffect
    data class NavigateToProduct(val productId: Long) : ProductsSideEffect
    data object NavigateToNewProduct : ProductsSideEffect
    data object NavigateBack : ProductsSideEffect
}
