package com.bruno13palhano.mais1venda.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.mais1venda.ui.screens.home.presenter.HomeRoute
import kotlinx.serialization.Serializable

internal fun NavGraphBuilder.homeNavGraph(
    navController: NavController,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    openDrawer: () -> Unit,
) {
    navigation<MainRoutes.Home>(startDestination = HomeRoutes.MainHome) {
        composable<HomeRoutes.MainHome> {
            gesturesEnabled(true)
            HomeRoute(
                openDrawer = openDrawer,
                navigateToLogin = { navController.navigate(MainRoutes.Login) },
                navigateToProducts = { navController.navigate(HomeRoutes.Products) },
            )
        }

        productsNavGraph(
            navController = navController,
            gesturesEnabled = gesturesEnabled,
        )

        ordersStatusNavGraph(
            navController = navController,
            gesturesEnabled = gesturesEnabled,
        )
    }
}

internal sealed interface HomeRoutes {
    @Serializable
    data object MainHome : HomeRoutes

    @Serializable
    data object Products : HomeRoutes

    @Serializable
    data object OrdersStatus : HomeRoutes
}
