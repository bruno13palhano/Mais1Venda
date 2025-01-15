package com.bruno13palhano.mais1venda.ui.screens.settings.presenter

import androidx.compose.runtime.Immutable

@Immutable
internal data class SettingsState(
    val companyName: String = "",
)

internal sealed interface SettingsEvent {
    data object OpenMenu : SettingsEvent
}

internal sealed interface SettingsSideEffect {
    data object OpenMenu : SettingsSideEffect
}
