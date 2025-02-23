package com.bruno13palhano.mais1venda.ui.screens.ads.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.data.model.shared.Ad

@Immutable
internal data class AdsState(
    val ads: List<Ad> = emptyList(),
    val openOptionMenu: Boolean = false,
)

@Immutable
internal sealed interface AdsEvent {
    data class LoadAds(val ads: List<Ad>) : AdsEvent
    data object NewAd : AdsEvent
    data class EditAd(val id: Long) : AdsEvent
    data object OpenSearch : AdsEvent
    data class Search(val query: String) : AdsEvent
    data object ToggleOptionMenu : AdsEvent
    data class UpdateSelectedOption(val option: AdsMenuItems) : AdsEvent
    data object NavigateBack : AdsEvent
}

@Immutable
internal sealed interface AdsSideEffect {
    data object NavigateToNewAd : AdsSideEffect
    data class NavigateToEditAd(val id: Long) : AdsSideEffect
    data object NavigateBack : AdsSideEffect
}

internal enum class AdsMenuItems {
    SORT_BY_TITLE,
    SORT_BY_PRODUCT_NAME,
    SORT_BY_OFF,
    SORT_BY_PRICE,
    SORT_BY_UNITS_SOLD,
    SORT_BY_REVIEW,
}
