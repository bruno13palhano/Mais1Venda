package com.bruno13palhano.mais1venda.ui.screens.products.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.components.CustomFloatField
import com.bruno13palhano.mais1venda.ui.screens.components.CustomIntegerField
import com.bruno13palhano.mais1venda.ui.screens.components.CustomTextField
import com.bruno13palhano.mais1venda.ui.screens.products.viewmodel.NewProductViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.clickableWithoutRipple
import com.bruno13palhano.mais1venda.ui.screens.shared.currentTimestamp
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle
import com.bruno13palhano.mais1venda.ui.theme.Mais1VendaTheme
import kotlinx.coroutines.launch

@Composable
internal fun NewProductRoute(
    navigateBack: () -> Unit,
    viewModel: NewProductViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val inputFieldsErrorMessage = stringResource(id = R.string.invalid_fields)

    LaunchedEffect(effect) {
        effect.collect { sideEffect ->
            when (sideEffect) {
                NewProductSideEffect.DismissKeyboard -> {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }

                NewProductSideEffect.NavigateBack -> navigateBack()

                is NewProductSideEffect.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = inputFieldsErrorMessage,
                            withDismissAction = true,
                        )
                    }
                }
            }
        }
    }

    NewProductContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::handleEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewProductContent(
    state: ProductState,
    snackbarHostState: SnackbarHostState,
    onEvent: (event: NewProductEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .clickableWithoutRipple { }
            .consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.new_product)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(NewProductEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back),
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            NewProductForm(state = state, onEvent = onEvent)
        }
    }
}

@Composable
private fun NewProductForm(state: ProductState, onEvent: (event: NewProductEvent) -> Unit) {
    CustomTextField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = state.name,
        onValueChange = { onEvent(NewProductEvent.NameChanged(it)) },
        label = stringResource(R.string.product_name),
        placeholder = stringResource(R.string.product_name_placeholder),
        isError = state.nameError,
    )

    CustomFloatField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = state.price,
        onValueChange = { onEvent(NewProductEvent.PriceChanged(it)) },
        label = stringResource(R.string.price),
        placeholder = stringResource(R.string.price_placeholder),
        isError = state.priceError,
    )

    CustomTextField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = state.category,
        onValueChange = { onEvent(NewProductEvent.CategoryChanged(it)) },
        label = stringResource(R.string.category),
        placeholder = stringResource(R.string.category_placeholder),
        isError = state.categoryError,
    )

    CustomTextField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = state.description,
        onValueChange = { onEvent(NewProductEvent.DescriptionChanged(it)) },
        label = stringResource(R.string.description),
        placeholder = stringResource(R.string.description_placeholder),
        isError = state.descriptionError,
    )

    CustomIntegerField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = state.code,
        onValueChange = { onEvent(NewProductEvent.CodeChanged(it)) },
        label = stringResource(R.string.code),
        placeholder = stringResource(R.string.code_placeholder),
        isError = state.codeError,
    )

    CustomIntegerField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = state.quantity,
        onValueChange = { onEvent(NewProductEvent.QuantityChanged(it)) },
        label = stringResource(R.string.quantity),
        placeholder = stringResource(R.string.quantity_placeholder),
        isError = state.quantityError,
    )

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        IconToggleButton(
            checked = state.exhibitToCatalog,
            onCheckedChange = { onEvent(NewProductEvent.ToggleExhibitToCatalog) },
        ) {
            if (state.exhibitToCatalog) {
                Icon(
                    modifier = Modifier.size(64.dp),
                    imageVector = Icons.Filled.ToggleOn,
                    contentDescription = stringResource(R.string.exhibit_to_catalog),
                )
            } else {
                Icon(
                    modifier = Modifier.size(64.dp),
                    imageVector = Icons.Filled.ToggleOff,
                    contentDescription = stringResource(R.string.do_not_exhibit_to_catalog),
                )
            }
        }
    }

    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onClick = { onEvent(NewProductEvent.SaveProduct(timestamp = currentTimestamp())) },
    ) {
        Text(text = stringResource(R.string.ok))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NewProductPreview() {
    Mais1VendaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            NewProductContent(
                state = ProductState(),
                snackbarHostState = remember { SnackbarHostState() },
                onEvent = {},
            )
        }
    }
}
