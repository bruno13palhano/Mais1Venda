package com.bruno13palhano.mais1venda.ui.screens.products.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.ProductRep
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.mvi.Container
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.ProductsEvents
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.ProductsSideEffect
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.ProductsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ProductsViewModel @Inject constructor(
    initialState: ProductsState,
    @ProductRep private val productRepository: ProductRepository,
) : ViewModel() {
    val container = Container<ProductsState, ProductsSideEffect>(
        initialState = initialState,
        scope = viewModelScope,
    )

    fun handleEvent(event: ProductsEvents) {
        when (event) {
            ProductsEvents.LoadProducts -> loadProducts()

            ProductsEvents.OpenOptionsMenu -> openOptionsMenu()

            is ProductsEvents.OptionsItemSelected -> optionsItemSelected(option = event.option)

            is ProductsEvents.NavigateToProduct -> navigateToProduct(productId = event.productId)

            ProductsEvents.NavigateToNewProduct -> navigateToNewProduct()

            ProductsEvents.NavigateBack -> navigateBack()
        }
    }

    private fun loadProducts() = container.intent {
        val response = productRepository.getAll()

        when (response) {
            is Resource.Success -> {
                reduce { copy(products = response.data ?: emptyList()) }
            }

            is Resource.ResponseError -> {
            }

            is Resource.Error -> {
            }
        }
    }

    private fun openOptionsMenu() = container.intent {
        postSideEffect(effect = ProductsSideEffect.OpenOptionsMenu)
    }

    private fun optionsItemSelected(option: String) = container.intent {
        reduce { copy(selectedOption = option) }
    }

    private fun navigateToProduct(productId: Long) = container.intent {
        postSideEffect(effect = ProductsSideEffect.NavigateToProduct(productId = productId))
    }

    private fun navigateToNewProduct() = container.intent {
        postSideEffect(effect = ProductsSideEffect.NavigateToNewProduct)
    }

    private fun navigateBack() = container.intent {
        postSideEffect(effect = ProductsSideEffect.NavigateBack)
    }
}
