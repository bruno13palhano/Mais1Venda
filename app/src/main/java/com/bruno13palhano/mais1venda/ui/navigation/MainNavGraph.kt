package com.bruno13palhano.mais1venda.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginRoute
import com.bruno13palhano.mais1venda.ui.screens.home.presenter.HomeRoute
import kotlinx.serialization.Serializable

@Composable
internal fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
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

        composable<MainRoutes.Login> {
            LoginRoute(
                navigateToHome = { navController.navigate(MainRoutes.Home) },
                navigateToForgotPassword = {},
                navigateToCreateAccount = {}
            )
        }

        composable<MainRoutes.ForgotPassword> {

        }

        composable<MainRoutes.CreateAccount> {

        }

        composable<MainRoutes.Settings> {

        }
    }
}

internal sealed interface MainRoutes {
    @Serializable
    data object Home : MainRoutes

    @Serializable
    data object Login : MainRoutes

    @Serializable
    data object ForgotPassword : MainRoutes

    @Serializable
    data object CreateAccount : MainRoutes

    @Serializable
    data object Settings : MainRoutes
}
