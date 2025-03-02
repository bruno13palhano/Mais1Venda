package com.bruno13palhano.mais1venda.ui.screens.ads.presenter

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.ads.viewmodel.AdViewModel
import com.bruno13palhano.mais1venda.ui.screens.components.CircularProgress
import com.bruno13palhano.mais1venda.ui.screens.components.CustomClickField
import com.bruno13palhano.mais1venda.ui.screens.components.CustomDoubleField
import com.bruno13palhano.mais1venda.ui.screens.components.CustomFloatField
import com.bruno13palhano.mais1venda.ui.screens.components.CustomIntegerField
import com.bruno13palhano.mais1venda.ui.screens.components.CustomTextField
import com.bruno13palhano.mais1venda.ui.screens.components.testProduct
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle
import com.bruno13palhano.mais1venda.ui.theme.Mais1VendaTheme
import kotlinx.coroutines.launch

@Composable
internal fun NewAdRoute(navigateBack: () -> Unit, viewModel: AdViewModel = hiltViewModel()) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val inputFieldsErrorMessage = stringResource(id = R.string.invalid_fields)

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                is AdSideEffect.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = inputFieldsErrorMessage,
                            withDismissAction = true,
                        )
                    }
                }

                AdSideEffect.DismissKeyboard -> {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }

                AdSideEffect.NavigateBack -> navigateBack()
            }
        }
    }

    AdContent(
        snackbarHostState = snackbarHostState,
        state = state,
        onEvent = viewModel::handleEvent,
    )
}

@Composable
internal fun EditAdRoute(
    id: Long,
    navigateBack: () -> Unit,
    viewModel: AdViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val inputFieldsErrorMessage = stringResource(id = R.string.invalid_fields)

    LaunchedEffect(Unit) { viewModel.handleEvent(event = AdEvent.GetAd(id = id)) }

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                is AdSideEffect.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = inputFieldsErrorMessage,
                            withDismissAction = true,
                        )
                    }
                }

                AdSideEffect.DismissKeyboard -> {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }

                AdSideEffect.NavigateBack -> navigateBack()
            }
        }
    }

    AdContent(
        id = id,
        snackbarHostState = snackbarHostState,
        state = state,
        onEvent = viewModel::handleEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdContent(
    id: Long = 0L,
    snackbarHostState: SnackbarHostState,
    state: AdState,
    onEvent: (event: AdEvent) -> Unit,
) {
    val title = if (state.id == null) {
        stringResource(id = R.string.new_ad)
    } else {
        stringResource(id = R.string.edit_ad)
    }

    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(AdEvent.NavigateBack) }) {
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
        if (state.loading) {
            CircularProgress(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(it)
                    .consumeWindowInsets(it)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(256.dp)
                            .padding(8.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .align(alignment = Alignment.CenterHorizontally),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(data = state.product?.image)
                            .memoryCacheKey(key = "ad-${state.id}")
                            .build(),
                        contentDescription = stringResource(id = R.string.ad_image),
                        contentScale = ContentScale.Crop,
                    )
                }

                CustomTextField(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    value = state.title,
                    onValueChange = { title -> onEvent(AdEvent.TitleChanged(title = title)) },
                    label = stringResource(id = R.string.title),
                    placeholder = stringResource(id = R.string.title_placeholder),
                )

                Box {
                    CustomClickField(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        value = state.product?.name ?: "",
                        onClick = { onEvent(AdEvent.ToggleShowProductsOptions) },
                        label = stringResource(id = R.string.product_name),
                        placeholder = stringResource(id = R.string.product_name_placeholder),
                    )

                    ProductsOptionsDialog(
                        showOptions = state.showProductsOptions,
                        products = state.products,
                        onDismissRequest = { onEvent(AdEvent.ToggleShowProductsOptions) },
                        onClose = { onEvent(AdEvent.ToggleShowProductsOptions) },
                        onUpdateProduct = { product ->
                            onEvent(AdEvent.UpdateProduct(product = product))
                        },
                    )
                }

                CustomDoubleField(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    value = state.price,
                    onValueChange = { price -> onEvent(AdEvent.PriceChanged(price = price)) },
                    label = stringResource(id = R.string.price),
                    placeholder = stringResource(id = R.string.price_placeholder),
                )

                CustomFloatField(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    value = state.off,
                    onValueChange = { off -> onEvent(AdEvent.OffChanged(off = off)) },
                    label = stringResource(id = R.string.off),
                    placeholder = stringResource(id = R.string.off_placeholder),
                )

                CustomIntegerField(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    value = "",
                    onValueChange = { stockQuantity ->
                        onEvent(
                            AdEvent.StockQuantityChanged(stockQuantity = stockQuantity),
                        )
                    },
                    label = stringResource(id = R.string.stock_quantity),
                    placeholder = stringResource(id = R.string.stock_quantity_placeholder),
                )

                CustomTextField(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                        .sizeIn(minHeight = 256.dp),
                    value = state.description,
                    onValueChange = { description ->
                        onEvent(
                            AdEvent.DescriptionChanged(description = description),
                        )
                    },
                    label = stringResource(id = R.string.description),
                    placeholder = stringResource(id = R.string.description_placeholder),
                    singleLine = false,
                )

                CustomTextField(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                        .sizeIn(minHeight = 128.dp),
                    value = state.observations,
                    onValueChange = { observations ->
                        onEvent(
                            AdEvent.ObservationsChanged(observations = observations),
                        )
                    },
                    label = stringResource(id = R.string.observations),
                    placeholder = stringResource(id = R.string.observations_placeholder),
                    singleLine = false,
                )

                Button(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                        .fillMaxWidth(),
                    onClick = {
                        onEvent(AdEvent.Publish(id = id))
                    },
                ) {
                    Text(text = stringResource(R.string.publish))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ProductsOptionsDialog(
    showOptions: Boolean,
    products: List<Product>,
    onDismissRequest: () -> Unit,
    onClose: () -> Unit,
    onUpdateProduct: (product: Product) -> Unit,
) {
    if (showOptions) {
        BasicAlertDialog(
            modifier = Modifier
                .padding(vertical = 64.dp)
                .clip(shape = RoundedCornerShape(8.dp)),
            onDismissRequest = onDismissRequest,
        ) {
            Box {
                LazyColumn {
                    stickyHeader {
                        ListItem(
                            overlineContent = {
                                Text(text = stringResource(R.string.chose_a_product))
                            },
                            headlineContent = {
                                Text(
                                    text = stringResource(id = R.string.products),
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            },
                            trailingContent = {
                                IconButton(
                                    onClick = onClose,
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = null,
                                    )
                                }
                            },
                        )
                        HorizontalDivider()
                    }

                    items(items = products, key = { product -> product.id }) { product ->
                        ListItem(
                            modifier = Modifier
                                .clickable { onUpdateProduct(product) },
                            headlineContent = { Text(text = product.name) },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AdPreview() {
    Mais1VendaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            AdContent(
                snackbarHostState = remember { SnackbarHostState() },
                state = AdState(),
                onEvent = {},
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AdShowProductsPreview() {
    Mais1VendaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            AdContent(
                snackbarHostState = remember { SnackbarHostState() },
                state = AdState(
                    products = listOf(
                        testProduct.copy(id = 1, name = "Product 1"),
                        testProduct.copy(id = 2, name = "Product 2"),
                        testProduct.copy(id = 3, name = "Product 3"),
                        testProduct.copy(id = 4, name = "Product 4"),
                        testProduct.copy(id = 5, name = "Product 5"),
                        testProduct.copy(id = 6, name = "Product 6"),
                        testProduct.copy(id = 7, name = "Product 7"),
                        testProduct.copy(id = 8, name = "Product 8"),
                        testProduct.copy(id = 9, name = "Product 9"),
                        testProduct.copy(id = 10, name = "Product 10"),
                        testProduct.copy(id = 11, name = "Product 11"),
                        testProduct.copy(id = 12, name = "Product 12"),
                        testProduct.copy(id = 13, name = "Product 13"),
                        testProduct.copy(id = 14, name = "Product 14"),
                        testProduct.copy(id = 15, name = "Product 15"),
                    ),
                    showProductsOptions = true,
                ),
                onEvent = {},
            )
        }
    }
}
