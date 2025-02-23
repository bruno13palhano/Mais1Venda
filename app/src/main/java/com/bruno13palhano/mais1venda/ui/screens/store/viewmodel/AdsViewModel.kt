package com.bruno13palhano.mais1venda.ui.screens.store.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.model.shared.Ad
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.mais1venda.ui.screens.store.presenter.AdsEvent
import com.bruno13palhano.mais1venda.ui.screens.store.presenter.AdsMenuItems
import com.bruno13palhano.mais1venda.ui.screens.store.presenter.AdsSideEffect
import com.bruno13palhano.mais1venda.ui.screens.store.presenter.AdsState
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

    private fun loadAds(ads: List<Ad>) {
        TODO("Not yet implemented")
    }

    private fun newAd() {
        TODO("Not yet implemented")
    }

    private fun editAd(id: Long) {
        TODO("Not yet implemented")
    }

    private fun openSearch() {
        TODO("Not yet implemented")
    }

    private fun search(query: String) {
        TODO("Not yet implemented")
    }

    private fun toggleOptionMenu() {
        TODO("Not yet implemented")
    }

    private fun updateSelectedOption(option: AdsMenuItems) {
        TODO("Not yet implemented")
    }

    private fun navigateBack() {

    }
}
