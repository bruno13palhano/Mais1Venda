package com.bruno13palhano.mais1venda.ui.screens.ads.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.mais1venda.ui.screens.ads.presenter.AdEvent
import com.bruno13palhano.mais1venda.ui.screens.ads.presenter.AdSideEffect
import com.bruno13palhano.mais1venda.ui.screens.ads.presenter.AdState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class AdViewModel @Inject constructor(
    initialState: AdState
) : ViewModel() {
    val container = Container<AdState, AdSideEffect>(
        initialState = initialState,
        scope = viewModelScope
    )

    fun handleEvent(event: AdEvent) {
        when (event) {
            is AdEvent.GetAd -> {}

            is AdEvent.TitleChanged -> {}

            is AdEvent.UpdateProduct -> {}

            is AdEvent.DescriptionChanged -> {}

            is AdEvent.ObservationsChanged -> {}

            is AdEvent.OffChanged -> {}

            is AdEvent.UnitsSoldChanged -> {}

            is AdEvent.QuestionsChanged -> {}

            is AdEvent.ReviewsChanged -> {}

            AdEvent.NavigateBack -> {}
        }
    }
}
