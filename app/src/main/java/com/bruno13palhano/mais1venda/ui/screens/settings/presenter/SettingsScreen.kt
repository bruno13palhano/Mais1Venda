package com.bruno13palhano.mais1venda.ui.screens.settings.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.settings.viewmodel.SettingsViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle

@Composable
internal fun SettingsRoute(
    onIconMenuClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(flow = viewModel.container.sideEffect)

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                SettingsSideEffect.OpenMenu -> onIconMenuClick()
            }
        }
    }

    SettingsContent(
        state = state,
        onEvent = viewModel::handleEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsContent(
    state: SettingsState,
    onEvent: (event: SettingsEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(SettingsEvent.OpenMenu) }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(R.string.burger_menu),
                        )
                    }
                },
            )
        },
    ) {
        Column(modifier = Modifier.padding(it)) {

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsContentPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {

        }
    }
}
