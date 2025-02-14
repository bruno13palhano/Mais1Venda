package com.bruno13palhano.mais1venda.ui.screens.components

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.navigation.MainRoutes
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun DrawerMenu(
    drawerState: DrawerState,
    navController: NavHostController,
    gesturesEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val items =
        listOf(
            Screen.Settings,
        )
    val orientation = LocalConfiguration.current.orientation
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            ModalDrawerSheet(
                modifier = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Modifier.fillMaxWidth(.78F)
                } else {
                    Modifier
                },
                drawerShape = RectangleShape,
            ) {
                LazyColumn {
                    stickyHeader {
                        Text(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            text = stringResource(id = R.string.app_name),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.titleLarge,
                        )
                        HorizontalDivider()
                    }

                    items(items = items) { screen ->
                        val selected = currentDestination?.selectedRoute(screen = screen)

                        NavigationDrawerItem(
                            shape = RoundedCornerShape(0, 50, 50, 0),
                            icon = { Icon(imageVector = screen.icon, contentDescription = null) },
                            label = { Text(text = stringResource(id = screen.resourceId)) },
                            selected = selected == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    restoreState = true
                                    launchSingleTop = true
                                }
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            modifier = Modifier
                                .padding(top = 4.dp, bottom = 4.dp, end = 8.dp)
                                .height(52.dp),
                        )
                    }
                }
            }
        },
        content = content,
    )
}

// workaround to get the current selected route
internal fun NavDestination.selectedRoute(screen: Screen<MainRoutes>): Boolean {
    return hierarchy.any {
        it.route?.split(".")?.lastOrNull() == screen.route.toString()
    }
}

internal sealed class Screen<T>(
    val route: T,
    val icon: ImageVector,
    @StringRes val resourceId: Int,
) {
    data object Settings : Screen<MainRoutes>(
        route = MainRoutes.Settings,
        icon = Icons.Filled.Settings,
        resourceId = R.string.settings,
    )
}

@Composable
fun <T> MoreVertMenu(
    items: Map<String, T>,
    expanded: Boolean,
    onDismissRequest: (expanded: Boolean) -> Unit,
    onItemClick: (T) -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest(false) },
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = { Text(text = item.key) },
                onClick = {
                    onItemClick(item.value)
                    onDismissRequest(false)
                },
            )
        }
    }
}
