package com.bruno13palhano.mais1venda.ui.screens.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.mais1venda.ui.screens.settings.presenter.SettingsEvent
import com.bruno13palhano.mais1venda.ui.screens.settings.presenter.SettingsSideEffect
import com.bruno13palhano.mais1venda.ui.screens.settings.presenter.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    initialState: SettingsState
) : ViewModel() {
    val container = Container<SettingsState, SettingsSideEffect>(
        initialState = initialState,
        scope = viewModelScope
    )

    fun handleEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.OpenMenu -> openMenu()
        }
    }

    private fun openMenu() = container.intent {
        postSideEffect(SettingsSideEffect.OpenMenu)
    }
}
