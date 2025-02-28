package com.bruno13palhano.mais1venda.ui.screens.ads.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError

@Immutable
internal data class AdState(
    val loading: Boolean = false,
    val id: Long? = null,
    val title: String = "",
    val products: List<Product> = emptyList(),
    val product: Product? = null,
    val price: String = "",
    val off: String = "",
    val stockQuantity: String = "",
    val description: String = "",
    val observations: String = "",
    val showProductsOptions: Boolean = false,
    val lastModifiedTimestamp: String = "",
    val titleError: Boolean = false,
    val descriptionError: Boolean = false,
)

@Immutable
internal sealed interface AdEvent {
    data class GetAd(val id: Long) : AdEvent
    data class TitleChanged(val title: String) : AdEvent
    data object GetProducts : AdEvent
    data object ToggleShowProductsOptions : AdEvent
    data class UpdateProduct(val product: Product) : AdEvent
    data class PriceChanged(val price: String) : AdEvent
    data class OffChanged(val off: String) : AdEvent
    data class StockQuantityChanged(val stockQuantity: String) : AdEvent
    data class DescriptionChanged(val description: String) : AdEvent
    data class ObservationsChanged(val observations: String) : AdEvent
    data class Publish(val id: Long) : AdEvent
    data object NavigateBack : AdEvent
}

@Immutable
internal sealed interface AdSideEffect {
    data object DismissKeyboard : AdSideEffect
    data object NavigateBack : AdSideEffect
    data class ShowError(val codeError: CodeError?) : AdSideEffect
}
