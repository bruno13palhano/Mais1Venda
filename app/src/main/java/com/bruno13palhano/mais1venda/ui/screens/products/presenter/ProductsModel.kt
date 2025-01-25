package com.bruno13palhano.mais1venda.ui.screens.products.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.data.model.company.Product

@Immutable
internal data class ProductsState(
    val products: List<Product> = emptyList(),
)

@Immutable
internal sealed interface ProductsEvents {
    data object LoadProducts : ProductsEvents
    data object NavigateBack : ProductsEvents
}

@Immutable
internal sealed interface ProductsSideEffect {

}
