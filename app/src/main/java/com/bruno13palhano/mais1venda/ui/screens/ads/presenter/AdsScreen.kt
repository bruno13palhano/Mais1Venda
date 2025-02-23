package com.bruno13palhano.mais1venda.ui.screens.ads.presenter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import com.bruno13palhano.data.model.shared.Ad
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.components.MoreVertMenu
import com.bruno13palhano.mais1venda.ui.screens.components.testProduct
import com.bruno13palhano.mais1venda.ui.screens.shared.rememberFlowWithLifecycle
import com.bruno13palhano.mais1venda.ui.screens.ads.viewmodel.AdsViewModel
import com.bruno13palhano.mais1venda.ui.theme.Mais1VendaTheme

@Composable
internal fun AdsRoute(
    navigateToNewAd: () -> Unit,
    navigateToEditAd: (id: Long) -> Unit,
    navigateBack: () -> Unit,
    viewModel: AdsViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(viewModel.container.sideEffect)

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                AdsSideEffect.NavigateToNewAd -> navigateToNewAd()

                is AdsSideEffect.NavigateToEditAd -> navigateToEditAd(effect.id)

                AdsSideEffect.NavigateBack -> navigateBack()
            }
        }
    }

    AdsContent(
        state = state,
        onEvent = viewModel::handleEvents
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdsContent(
    state: AdsState,
    onEvent: (event: AdsEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.ads)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(AdsEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(AdsEvent.OpenSearch) }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(R.string.search),
                        )
                    }

                    Box {
                        IconButton(onClick = { onEvent(AdsEvent.ToggleOptionMenu) }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(R.string.more_options_menu),
                            )
                        }

                        val items = getMoreOptionsItem()

                        MoreVertMenu(
                            items = items,
                            expanded = state.openOptionMenu,
                            onDismissRequest = { onEvent(AdsEvent.ToggleOptionMenu) },
                            onItemClick = { onEvent(AdsEvent.UpdateSelectedOption(option = it)) }
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(AdsEvent.NewAd) }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_new_ad),
                )
            }
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it),
            contentPadding = PaddingValues(4.dp),
        ) {
            items(items = state.ads, key = { ad -> ad.id }) { ad ->
                ListItem(
                    modifier = Modifier.clickable { onEvent(AdsEvent.NewAd) },
                    overlineContent = { Text(text = ad.product.name) },
                    headlineContent = { Text(text = ad.title) },
                    supportingContent = { Text(text = ad.description) },
                    trailingContent = { Text(text = stringResource(R.string.price_tag, ad.product.price)) },
                )
            }
        }
    }
}

@Composable
private fun getMoreOptionsItem(): Map<String, AdsMenuItems> {
    return mapOf(
        stringResource(id = R.string.title) to AdsMenuItems.SORT_BY_TITLE,
        stringResource(id = R.string.product_name) to AdsMenuItems.SORT_BY_PRODUCT_NAME,
        stringResource(id = R.string.off) to AdsMenuItems.SORT_BY_OFF,
        stringResource(id = R.string.units_sold) to AdsMenuItems.SORT_BY_UNITS_SOLD,
        stringResource(id = R.string.review) to AdsMenuItems.SORT_BY_REVIEW,
        stringResource(id = R.string.price) to AdsMenuItems.SORT_BY_PRICE,
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AdsPreview() {
    Mais1VendaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            AdsContent(
                state = AdsState(
                    ads = listOf(
                        Ad(
                            id = 1,
                            title = "Ad 1",
                            product = testProduct,
                            description = "Ad decription",
                            observations = "",
                            off = 1.5f,
                            unitsSold = 1,
                            questions = emptyList(),
                            reviews = emptyList(),
                            lastModifiedTimestamp = ""
                        ),
                        Ad(
                            id = 2,
                            title = "Ad 2",
                            product = testProduct,
                            description = "Ad decription",
                            observations = "",
                            off = 1.5f,
                            unitsSold = 1,
                            questions = emptyList(),
                            reviews = emptyList(),
                            lastModifiedTimestamp = ""
                        ),
                        Ad(
                            id = 3,
                            title = "Ad 3",
                            product = testProduct,
                            description = "Ad decription",
                            observations = "",
                            off = 1.5f,
                            unitsSold = 1,
                            questions = emptyList(),
                            reviews = emptyList(),
                            lastModifiedTimestamp = ""
                        ),
                        Ad(
                            id = 4,
                            title = "Ad 4",
                            product = testProduct,
                            description = "Ad decription",
                            observations = "",
                            off = 1.5f,
                            unitsSold = 1,
                            questions = emptyList(),
                            reviews = emptyList(),
                            lastModifiedTimestamp = ""
                        ),
                        Ad(
                            id = 5,
                            title = "Ad 5",
                            product = testProduct,
                            description = "Ad decription",
                            observations = "",
                            off = 1.5f,
                            unitsSold = 1,
                            questions = emptyList(),
                            reviews = emptyList(),
                            lastModifiedTimestamp = ""
                        ),
                        Ad(
                            id = 6,
                            title = "Ad 6",
                            product = testProduct,
                            description = "Ad decription",
                            observations = "",
                            off = 1.5f,
                            unitsSold = 1,
                            questions = emptyList(),
                            reviews = emptyList(),
                            lastModifiedTimestamp = ""
                        ),
                        Ad(
                            id = 7,
                            title = "Ad 7",
                            product = testProduct,
                            description = "Ad decription",
                            observations = "",
                            off = 1.5f,
                            unitsSold = 1,
                            questions = emptyList(),
                            reviews = emptyList(),
                            lastModifiedTimestamp = ""
                        ),
                        Ad(
                            id = 8,
                            title = "Ad 8",
                            product = testProduct,
                            description = "Ad decription",
                            observations = "",
                            off = 1.5f,
                            unitsSold = 1,
                            questions = emptyList(),
                            reviews = emptyList(),
                            lastModifiedTimestamp = ""
                        ),
                        Ad(
                            id = 9,
                            title = "Ad 9",
                            product = testProduct,
                            description = "Ad decription",
                            observations = "",
                            off = 1.5f,
                            unitsSold = 1,
                            questions = emptyList(),
                            reviews = emptyList(),
                            lastModifiedTimestamp = ""
                        ),
                        Ad(
                            id = 10,
                            title = "Ad 10",
                            product = testProduct,
                            description = "Ad decription",
                            observations = "",
                            off = 1.5f,
                            unitsSold = 1,
                            questions = emptyList(),
                            reviews = emptyList(),
                            lastModifiedTimestamp = ""
                        ),
                    )
                ),
                onEvent = {},
            )
        }
    }
}
