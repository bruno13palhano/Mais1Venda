package com.bruno13palhano.mais1venda.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.EditProductRoute
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.NewProductRoute
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.ProductsRoute
import kotlinx.serialization.Serializable

internal fun NavGraphBuilder.productsNavGraph(
    navController: NavController,
    gesturesEnabled: (enabled: Boolean) -> Unit,
) {
    gesturesEnabled(false)

    navigation<HomeRoutes.Products>(startDestination = ProductsRoutes.MainProducts) {
        composable<ProductsRoutes.MainProducts> {
            ProductsRoute(
                navigateToProduct = { navController.navigate(ProductsRoutes.Edit(id = it)) },
                navigateToNewProduct = { navController.navigate(ProductsRoutes.New) },
                navigateBack = { navController.navigateUp() },
            )
        }

        composable<ProductsRoutes.New> {
            NewProductRoute(navigateBack = { navController.navigateUp() })
        }

        composable<ProductsRoutes.Edit> {
            val id = it.toRoute<ProductsRoutes.Edit>().id

            EditProductRoute(
                id = id,
                navigateBack = { navController.navigateUp() },
            )
        }
    }
}

internal sealed interface ProductsRoutes {
    @Serializable
    data object MainProducts : ProductsRoutes

    @Serializable
    data object New : ProductsRoutes

    @Serializable
    data class Edit(val id: Long) : ProductsRoutes
}
