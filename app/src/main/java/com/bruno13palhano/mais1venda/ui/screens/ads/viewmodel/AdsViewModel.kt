package com.bruno13palhano.mais1venda.ui.screens.ads.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.model.shared.Ad
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.mais1venda.ui.screens.ads.presenter.AdsEvent
import com.bruno13palhano.mais1venda.ui.screens.ads.presenter.AdsMenuItems
import com.bruno13palhano.mais1venda.ui.screens.ads.presenter.AdsSideEffect
import com.bruno13palhano.mais1venda.ui.screens.ads.presenter.AdsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class AdsViewModel @Inject constructor(
    initialState: AdsState,
) : ViewModel() {
    val container = Container<AdsState, AdsSideEffect>(
        initialState = initialState,
        scope = viewModelScope,
    )

    fun handleEvents(event: AdsEvent) {
        when (event) {
            is AdsEvent.LoadAds -> loadAds(ads = event.ads)

            AdsEvent.NewAd -> newAd()

            is AdsEvent.EditAd -> editAd(id = event.id)

            AdsEvent.OpenSearch -> openSearch()

            is AdsEvent.Search -> search(query = event.query)

            AdsEvent.ToggleOptionMenu -> toggleOptionMenu()

            is AdsEvent.UpdateSelectedOption -> updateSelectedOption(option = event.option)

            AdsEvent.NavigateBack -> navigateBack()
        }
    }

    private fun loadAds(ads: List<Ad>) = container.intent {
        reduce { copy(ads = ads) }
    }

    private fun newAd() = container.intent {
        postSideEffect(effect = AdsSideEffect.NavigateToNewAd)
    }

    private fun editAd(id: Long) = container.intent {
        postSideEffect(effect = AdsSideEffect.NavigateToEditAd(id = id))
    }

    private fun openSearch() = container.intent {
    }

    private fun search(query: String) = container.intent {
    }

    private fun toggleOptionMenu() = container.intent {
        reduce { copy(openOptionMenu = !openOptionMenu) }
    }

    private fun updateSelectedOption(option: AdsMenuItems) = container.intent {
        when (option) {
            AdsMenuItems.SORT_BY_TITLE -> {
                reduce { copy(ads = ads.sortedBy { it.title }, openOptionMenu = false) }
            }

            AdsMenuItems.SORT_BY_PRODUCT_NAME -> {
                reduce { copy(ads = ads.sortedBy { it.product.name }, openOptionMenu = false) }
            }

            AdsMenuItems.SORT_BY_OFF -> {
                reduce { copy(ads = ads.sortedBy { it.off }, openOptionMenu = false) }
            }

            AdsMenuItems.SORT_BY_PRICE -> {
                reduce { copy(ads = ads.sortedBy { it.price }, openOptionMenu = false) }
            }
        }
    }

    private fun navigateBack() = container.intent {
        postSideEffect(effect = AdsSideEffect.NavigateBack)
    }
}
