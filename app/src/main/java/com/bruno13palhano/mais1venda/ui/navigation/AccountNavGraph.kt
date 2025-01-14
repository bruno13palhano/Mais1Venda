package com.bruno13palhano.mais1venda.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountRoute
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginRoute
import kotlinx.serialization.Serializable

internal fun NavGraphBuilder.loginNavGraph(
    navController: NavController,
    gesturesEnabled: (enabled: Boolean) -> Unit,
) {
    navigation<MainRoutes.Login>(startDestination = LoginRoutes.MainLogin) {
        composable<LoginRoutes.MainLogin> {
            gesturesEnabled(false)
            LoginRoute(
                navigateToHome = {
                    navController.navigate(route = MainRoutes.Home) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                navigateToForgotPassword = {
                    navController.navigate(LoginRoutes.ForgotPassword)
                },
                navigateToCreateAccount = {
                    navController.navigate(LoginRoutes.CreateAccount)
                },
            )
        }

        composable<LoginRoutes.ForgotPassword> {
            gesturesEnabled(false)
        }

        composable<LoginRoutes.CreateAccount> {
            gesturesEnabled(false)
            CreateAccountRoute(
                navigateToHome = {
                    navController.navigate(route = MainRoutes.Home) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                navigateBack = { navController.navigateUp() },
            )
        }
    }
}

internal sealed interface LoginRoutes {
    @Serializable
    data object MainLogin : LoginRoutes

    @Serializable
    data object ForgotPassword : LoginRoutes

    @Serializable
    data object CreateAccount : LoginRoutes
}
