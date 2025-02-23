package com.bruno13palhano.mais1venda.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.mais1venda.ui.screens.store.presenter.AdsRoute
import kotlinx.serialization.Serializable

internal fun NavGraphBuilder.adsNavGraph(
    navController: NavController,
    gesturesEnabled: (enabled: Boolean) -> Unit,
) {
    gesturesEnabled(false)

    navigation<HomeRoutes.Ads>(startDestination = AdsRoutes.MainAds) {
        composable<AdsRoutes.MainAds> {
            AdsRoute(
                navigateToNewAd = { navController.navigate(AdsRoutes.NewAd) },
                navigateToEditAd = { id -> navController.navigate(AdsRoutes.EditAd(id)) },
                navigateBack = { navController.navigateUp() },
            )
        }

        composable<AdsRoutes.NewAd> {}

        composable<AdsRoutes.EditAd> {}
    }
}

internal sealed interface AdsRoutes {
    @Serializable
    data object MainAds : AdsRoutes

    @Serializable
    data object NewAd : AdsRoutes

    @Serializable
    data class EditAd(val id: Long) : AdsRoutes
}
