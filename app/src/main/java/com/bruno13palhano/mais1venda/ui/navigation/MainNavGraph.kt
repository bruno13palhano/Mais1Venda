package com.bruno13palhano.mais1venda.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bruno13palhano.mais1venda.ui.screens.home.presenter.HomeRoute
import com.bruno13palhano.mais1venda.ui.screens.settings.presenter.SettingsRoute
import kotlinx.serialization.Serializable

@Composable
internal fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onIconMenuClick: () -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MainRoutes.Home,
    ) {
        composable<MainRoutes.Home> {
            HomeRoute(
                navigateToLogin = { navController.navigate(MainRoutes.Login) },
            )
        }

        accountNavGraph(
            navController = navController,
            gesturesEnabled = gesturesEnabled,
        )

        composable<MainRoutes.Settings> {
            SettingsRoute(onIconMenuClick = onIconMenuClick)
        }
    }
}

internal sealed interface MainRoutes {
    @Serializable
    data object Home : MainRoutes

    @Serializable
    data object Login : MainRoutes

    @Serializable
    data object Settings : MainRoutes
}
