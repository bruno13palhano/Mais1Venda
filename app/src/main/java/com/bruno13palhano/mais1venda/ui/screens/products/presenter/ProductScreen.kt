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
import androidx.compose.material.icons.filled.MoreVert
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
import com.bruno13palhano.mais1venda.ui.screens.components.MoreVertMenu
import com.bruno13palhano.mais1venda.ui.screens.products.viewmodel.ProductViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.clickableWithoutRipple
import com.bruno13palhano.mais1venda.ui.screens.shared.currentTimestamp
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle
import com.bruno13palhano.mais1venda.ui.theme.Mais1VendaTheme
import kotlinx.coroutines.launch

@Composable
internal fun NewProductRoute(
    navigateBack: () -> Unit,
    viewModel: ProductViewModel = hiltViewModel(),
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
                ProductSideEffect.DismissKeyboard -> {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }

                ProductSideEffect.NavigateBack -> navigateBack()

                is ProductSideEffect.ShowError -> {
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

    ProductContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::handleEvent,
    )
}

@Composable
internal fun EditProductRoute(
    id: Long,
    navigateBack: () -> Unit,
    viewModel: ProductViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val inputFieldsErrorMessage = stringResource(id = R.string.invalid_fields)

    LaunchedEffect(Unit) { viewModel.handleEvent(event = ProductEvent.GetProduct(id = id)) }

    LaunchedEffect(effect) {
        effect.collect { sideEffect ->
            when (sideEffect) {
                ProductSideEffect.DismissKeyboard -> {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }

                ProductSideEffect.NavigateBack -> navigateBack()

                is ProductSideEffect.ShowError -> {
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

    ProductContent(
        id = id,
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::handleEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductContent(
    id: Long = 0L,
    state: ProductState,
    snackbarHostState: SnackbarHostState,
    onEvent: (event: ProductEvent) -> Unit,
) {
    val title = if (id == 0L) {
        stringResource(id = R.string.new_product)
    } else {
        stringResource(id = R.string.edit_product)
    }

    Scaffold(
        modifier = Modifier
            .clickableWithoutRipple { onEvent(ProductEvent.DismissKeyboard) }
            .consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = title)
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(ProductEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back),
                        )
                    }
                },
                actions = {
                    if (id != 0L) {
                        IconButton(onClick = { onEvent(ProductEvent.ToggleOptionsMenu) }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(
                                    id = R.string.more_options_menu,
                                ),
                            )

                            val items = getMoreOptionsItems()

                            MoreVertMenu(
                                items = items,
                                expanded = state.openOptionsMenu,
                                onDismissRequest = { onEvent(ProductEvent.ToggleOptionsMenu) },
                                onItemClick = {
                                    onEvent(ProductEvent.UpdateSelectedOption(option = it))
                                },
                            )
                        }
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
            ProductForm(id = id, state = state, onEvent = onEvent)
        }
    }
}

@Composable
private fun ProductForm(id: Long, state: ProductState, onEvent: (event: ProductEvent) -> Unit) {
    CustomTextField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = state.name,
        onValueChange = { onEvent(ProductEvent.NameChanged(it)) },
        label = stringResource(R.string.product_name),
        placeholder = stringResource(R.string.product_name_placeholder),
        isError = state.nameError,
    )

    CustomFloatField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = state.price,
        onValueChange = { onEvent(ProductEvent.PriceChanged(it)) },
        label = stringResource(R.string.price),
        placeholder = stringResource(R.string.price_placeholder),
        isError = state.priceError,
    )

    CustomTextField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = state.category,
        onValueChange = { onEvent(ProductEvent.CategoryChanged(it)) },
        label = stringResource(R.string.category),
        placeholder = stringResource(R.string.category_placeholder),
        isError = state.categoryError,
    )

    CustomTextField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = state.description,
        onValueChange = { onEvent(ProductEvent.DescriptionChanged(it)) },
        label = stringResource(R.string.description),
        placeholder = stringResource(R.string.description_placeholder),
        isError = state.descriptionError,
    )

    CustomIntegerField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = state.code,
        onValueChange = { onEvent(ProductEvent.CodeChanged(it)) },
        label = stringResource(R.string.code),
        placeholder = stringResource(R.string.code_placeholder),
        isError = state.codeError,
    )

    CustomIntegerField(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        value = state.quantity,
        onValueChange = { onEvent(ProductEvent.QuantityChanged(it)) },
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
            onCheckedChange = { onEvent(ProductEvent.ToggleExhibitToCatalog) },
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
        onClick = { onEvent(ProductEvent.SaveProduct(timestamp = currentTimestamp(), id = id)) },
    ) {
        Text(text = stringResource(R.string.ok))
    }
}

@Composable
private fun getMoreOptionsItems(): Map<String, ProductMenuItems> {
    return mapOf(
        stringResource(id = R.string.delete) to ProductMenuItems.DELETE,
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NewProductPreview() {
    Mais1VendaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            ProductContent(
                id = 0L,
                state = ProductState(),
                snackbarHostState = remember { SnackbarHostState() },
                onEvent = {},
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EditProductPreview() {
    Mais1VendaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            ProductContent(
                id = 1L,
                state = ProductState(),
                snackbarHostState = remember { SnackbarHostState() },
                onEvent = {},
            )
        }
    }
}
