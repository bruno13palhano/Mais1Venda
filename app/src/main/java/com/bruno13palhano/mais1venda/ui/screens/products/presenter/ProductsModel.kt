package com.bruno13palhano.mais1venda.ui.screens.products.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.data.model.company.Product

@Immutable
internal data class ProductsState(
    val products: List<Product> = emptyList(),
    val selectedOption: String? = null,
)

@Immutable
internal sealed interface ProductsEvents {
    data object LoadProducts : ProductsEvents
    data object OpenOptionsMenu : ProductsEvents
    data class OptionsItemSelected(val option: String) : ProductsEvents
    data class NavigateToProduct(val productId: Long) : ProductsEvents
    data object NavigateBack : ProductsEvents
}

@Immutable
internal sealed interface ProductsSideEffect {
    data object OpenOptionsMenu : ProductsSideEffect
    data class NavigateToProduct(val productId: Long) : ProductsSideEffect
    data object NavigateBack : ProductsSideEffect
}
