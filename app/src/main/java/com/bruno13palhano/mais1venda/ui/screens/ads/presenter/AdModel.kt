package com.bruno13palhano.mais1venda.ui.screens.ads.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError

@Immutable
internal data class AdState(
    val id: Long? = null,
    val title: String = "",
    val product: Product? = null,
    val description: String = "",
    val observations: String = "",
    val off: Float = 0f,
    val unitsSold: Int = 0,
    val questions: List<String> = emptyList(),
    val reviews: List<String> = emptyList(),
    val lastModifiedTimestamp: String = "",
    val titleError: Boolean = false,
    val descriptionError: Boolean = false,
)

@Immutable
internal sealed interface AdEvent {
    data class GetAd(val id: Long) : AdEvent
    data class TitleChanged(val title: String) : AdEvent
    data class UpdateProduct(val product: Product) : AdEvent
    data class DescriptionChanged(val description: String) : AdEvent
    data class ObservationsChanged(val observations: String) : AdEvent
    data class OffChanged(val off: Float) : AdEvent
    data class UnitsSoldChanged(val unitsSold: Int) : AdEvent
    data class QuestionsChanged(val questions: List<String>) : AdEvent
    data class ReviewsChanged(val reviews: List<String>) : AdEvent
    data object NavigateBack : AdEvent
}

@Immutable
internal sealed interface AdSideEffect {
    data object DismissKeyboard : AdSideEffect
    data object NavigateBack : AdSideEffect
    data class ShowError(val codeError: CodeError?) : AdSideEffect
}
