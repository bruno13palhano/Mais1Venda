package com.bruno13palhano.mais1venda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.bruno13palhano.mais1venda.ui.navigation.MainNavGraph
import com.bruno13palhano.mais1venda.ui.screens.components.DrawerMenu
import com.bruno13palhano.mais1venda.ui.theme.Mais1VendaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            var gesturesEnabled by rememberSaveable { mutableStateOf(false) }

            Mais1VendaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    DrawerMenu(
                        navController = navController,
                        drawerState = drawerState,
                    ) {
                        val scope = rememberCoroutineScope()

                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            MainNavGraph(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController,
                                gesturesEnabled = { enabled -> gesturesEnabled = enabled },
                                onIconMenuCLick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
