package com.bruno13palhano.mais1venda.ui.navigation

import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.CustomersRoute
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrderRoute
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrdersRoute
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersDashboardRoute
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersRoute
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersStatusRoute
import kotlinx.serialization.Serializable

internal fun NavGraphBuilder.ordersStatusNavGraph(
    navController: NavController,
    gesturesEnabled: (enabled: Boolean) -> Unit,
) {
    gesturesEnabled(false)

    navigation<HomeRoutes.OrdersStatus>(startDestination = OrdersStatusRoutes.MainOrdersStatus) {
        composable<OrdersStatusRoutes.MainOrdersStatus> {
            OrdersStatusRoute(
                navigateToNewOrders = { navController.navigate(OrdersStatusRoutes.NewOrders) },
                navigateToOrders = { navController.navigate(OrdersStatusRoutes.Orders) },
                navigateToCustomers = { navController.navigate(OrdersStatusRoutes.Customers) },
                navigateToDashboard = {
                    navController.navigate(
                        OrdersStatusRoutes.OrdersDashboard,
                    )
                },
                navigateBack = { navController.navigateUp() },
            )
        }

        composable<OrdersStatusRoutes.NewOrders> {
            NewOrdersRoute(
                navigateToNewOrder = {
                    navController.navigate(OrdersStatusRoutes.NewOrder(id = it))
                },
                navigateBack = { navController.navigateUp() },
            )
        }

        composable<OrdersStatusRoutes.Orders> {
            OrdersRoute(navigateBack = { navController.navigateUp() })
        }

        composable<OrdersStatusRoutes.Customers> {
            CustomersRoute(navigateBack = { navController.navigateUp() })
        }

        composable<OrdersStatusRoutes.NewOrder>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "mais1venda://orders/newOrder/{id}"
                    action = Intent.ACTION_VIEW
                },
            ),
        ) {
            val id = it.toRoute<OrdersStatusRoutes.NewOrder>().id

            NewOrderRoute(
                id = id,
                navigateBack = { navController.navigateUp() },
            )
        }

        composable<OrdersStatusRoutes.OrdersDashboard> {
            OrdersDashboardRoute(navigateBack = { navController.navigateUp() })
        }
    }
}

internal sealed interface OrdersStatusRoutes {
    @Serializable
    data object MainOrdersStatus : OrdersStatusRoutes

    @Serializable
    data object NewOrders : OrdersStatusRoutes

    @Serializable
    data object Orders : OrdersStatusRoutes

    @Serializable
    data object Customers : OrdersStatusRoutes

    @Serializable
    data class NewOrder(val id: Long) : OrdersStatusRoutes

    @Serializable
    data object OrdersDashboard : OrdersStatusRoutes
}
