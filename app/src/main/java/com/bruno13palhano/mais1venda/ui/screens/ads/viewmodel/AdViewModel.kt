package com.bruno13palhano.mais1venda.ui.screens.ads.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.model.shared.Ad
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.data.repository.AdRepository
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.mais1venda.ui.screens.ads.presenter.AdEvent
import com.bruno13palhano.mais1venda.ui.screens.ads.presenter.AdSideEffect
import com.bruno13palhano.mais1venda.ui.screens.ads.presenter.AdState
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class AdViewModel @Inject constructor(
    initialState: AdState,
    private val adRepository: AdRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {
    val container = Container<AdState, AdSideEffect>(
        initialState = initialState,
        scope = viewModelScope,
    )

    fun handleEvent(event: AdEvent) {
        when (event) {
            is AdEvent.GetAd -> getAd(id = event.id)

            is AdEvent.TitleChanged -> titleChanged(title = event.title)

            AdEvent.GetProducts -> getProducts()

            AdEvent.ToggleShowProductsOptions -> toggleShowProductsOptions()

            is AdEvent.UpdateProduct -> updateProduct(product = event.product)

            is AdEvent.PriceChanged -> priceChanged(price = event.price)

            is AdEvent.OffChanged -> offChanged(off = event.off)

            is AdEvent.StockQuantityChanged -> stockQuantityChanged(
                stockQuantity = event.stockQuantity,
            )

            is AdEvent.DescriptionChanged -> descriptionChanged(description = event.description)

            is AdEvent.ObservationsChanged -> observationsChanged(observations = event.observations)

            is AdEvent.Publish -> publish(id = event.id)

            AdEvent.NavigateBack -> navigateBack()
        }
    }

    private fun getAd(id: Long) = container.intent {
        val ad = adRepository.get(id = id)
        reduce {
            copy(
                title = ad?.title.toString(),
                price = ad?.price.toString(),
                off = ad?.off.toString(),
                stockQuantity = ad?.stockQuantity.toString(),
                description = ad?.description ?: "",
                observations = ad?.observations ?: "",
            )
        }
    }

    private fun getProducts() = container.intent {
        productRepository.getAll().collect { products ->
            reduce { copy(products = products) }
        }
    }

    private fun toggleShowProductsOptions() = container.intent {
        reduce { copy(showProductsOptions = !showProductsOptions) }
    }

    private fun updateProduct(product: Product) = container.intent {
        reduce { copy(product = product) }
    }

    private fun titleChanged(title: String) = container.intent {
        reduce { copy(title = title) }
    }

    private fun priceChanged(price: String) = container.intent {
        reduce { copy(price = price) }
    }

    private fun offChanged(off: String) = container.intent {
        reduce { copy(off = off) }
    }

    private fun stockQuantityChanged(stockQuantity: String) = container.intent {
        reduce { copy(stockQuantity = stockQuantity) }
    }

    private fun descriptionChanged(description: String) = container.intent {
        reduce { copy(description = description) }
    }

    private fun observationsChanged(observations: String) = container.intent {
        reduce { copy(observations = observations) }
    }

    private fun publish(id: Long) = container.intent {
        reduce { copy(loading = true) }

        val response = adRepository.insert(
            ad = Ad(
                id = id,
                title = container.state.value.title,
                product = container.state.value.product!!,
                price = container.state.value.price.toDouble(),
                description = container.state.value.description,
                observations = container.state.value.observations,
                off = container.state.value.off.toFloat(),
                stockQuantity = container.state.value.stockQuantity.toInt(),
                lastModifiedTimestamp = container.state.value.lastModifiedTimestamp,
            ),
        )

        when (response) {
            is Resource.Success -> {
                reduce { copy(loading = false) }

                val success: Boolean? = response.data
                if (success != null) {
                    if (success) {
                        postSideEffect(effect = AdSideEffect.NavigateBack)
                    } else {
                        // TODO: Handle error
                        postSideEffect(
                            effect = AdSideEffect.ShowError(codeError = CodeError.UNKNOWN_ERROR),
                        )
                    }
                } else {
                    // TODO: Handle error
                    postSideEffect(
                        effect = AdSideEffect.ShowError(codeError = CodeError.UNKNOWN_ERROR),
                    )
                }
            }

            is Resource.ResponseError -> {
                reduce { copy(loading = false) }
                postSideEffect(effect = AdSideEffect.ShowError(codeError = CodeError.UNKNOWN_ERROR))
            }

            is Resource.Error -> {
                reduce { copy(loading = false) }
                postSideEffect(effect = AdSideEffect.ShowError(codeError = CodeError.UNKNOWN_ERROR))
            }
        }
    }

    private fun navigateBack() = container.intent {
        postSideEffect(effect = AdSideEffect.NavigateBack)
    }
}
