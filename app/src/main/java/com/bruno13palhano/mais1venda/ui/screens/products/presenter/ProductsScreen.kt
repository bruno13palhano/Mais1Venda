package com.bruno13palhano.mais1venda.ui.screens.products.presenter

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.products.viewmodel.ProductsViewModel
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle

@Composable
internal fun ProductsRoute(
    navigateToProduct: (id: Long) -> Unit,
    navigateBack: () -> Unit,
    viewModel: ProductsViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    LaunchedEffect(Unit) {
        sideEffect.collect { effect ->
            when (effect) {
                ProductsSideEffect.OpenOptionsMenu -> {}

                is ProductsSideEffect.NavigateToProduct -> navigateToProduct(effect.productId)

                ProductsSideEffect.NavigateBack -> navigateBack()
            }
        }
    }

    ProductsContent(
        state = state,
        onEvent = viewModel::handleEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductsContent(state: ProductsState, onEvent: (even: ProductsEvents) -> Unit) {
    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.products)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(ProductsEvents.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { ProductsEvents.OpenOptionsMenu }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = stringResource(R.string.more_options_menu),
                        )
                    }
                },
            )
        },
    ) {
        LazyColumn (
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it),
            contentPadding = PaddingValues(4.dp),
        ) {
            items(items = state.products, key = { product -> product.id }) {
                ListItem(
                    modifier = Modifier.fillMaxSize(),
                    headlineContent = { Text(text = it.name) },
                    supportingContent = { Text(text = it.description) },
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProductsContentPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            ProductsContent(
                state = ProductsState(
                    products = productListPreview
                ),
                onEvent = {},
            )
        }
    }
}

private val productListPreview = listOf(
    Product(id = 1, name = "Product 1", description = "Description 1", price = 124.5F, category = listOf("Category 1"), code = "Code 1", quantity = 1, exhibitToCatalog = true, lastModifiedTimestamp = ""),
    Product(id = 2, name = "Product 2", description = "Description 2", price = 124.5F, category = listOf("Category 2"), code = "Code 2", quantity = 1, exhibitToCatalog = true, lastModifiedTimestamp = ""),
    Product(id = 3, name = "Product 3", description = "Description 3", price = 124.5F, category = listOf("Category 3"), code = "Code 3", quantity = 1, exhibitToCatalog = true, lastModifiedTimestamp = ""),
    Product(id = 4, name = "Product 4", description = "Description 4", price = 124.5F, category = listOf("Category 4"), code = "Code 4", quantity = 1, exhibitToCatalog = true, lastModifiedTimestamp = ""),
    Product(id = 5, name = "Product 5", description = "Description 5", price = 124.5F, category = listOf("Category 5"), code = "Code 5", quantity = 1, exhibitToCatalog = true, lastModifiedTimestamp = ""),
    Product(id = 6, name = "Product 6", description = "Description 6", price = 124.5F, category = listOf("Category 6"), code = "Code 6", quantity = 1, exhibitToCatalog = true, lastModifiedTimestamp = ""),
    Product(id = 7, name = "Product 7", description = "Description 7", price = 124.5F, category = listOf("Category 7"), code = "Code 7", quantity = 1, exhibitToCatalog = true, lastModifiedTimestamp = ""),
    Product(id = 8, name = "Product 8", description = "Description 8", price = 124.5F, category = listOf("Category 8"), code = "Code 8", quantity = 1, exhibitToCatalog = true, lastModifiedTimestamp = ""),
    Product(id = 9, name = "Product 9", description = "Description 9", price = 124.5F, category = listOf("Category 9"), code = "Code 9", quantity = 1, exhibitToCatalog = true, lastModifiedTimestamp = ""),
    Product(id = 10, name = "Product 10", description = "Description 10", price = 124.5F, category = listOf("Category 10"), code = "Code 10", quantity = 1, exhibitToCatalog = true, lastModifiedTimestamp = ""),
)
