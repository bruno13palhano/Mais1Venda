package com.bruno13palhano.mais1venda

import com.bruno13palhano.mais1venda.ui.screens.home.presenter.HomeEvent
import com.bruno13palhano.mais1venda.ui.screens.home.presenter.HomeSideEffect
import com.bruno13palhano.mais1venda.ui.screens.home.presenter.HomeState
import com.bruno13palhano.mais1venda.ui.screens.home.viewmodel.HomeViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class HomeViewModelTest {
    private val state = HomeState()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `OpenDrawer Event should emit OpenDrawer side effect`() = runTest {
        val homeViewModel = HomeViewModel(initialState = HomeState())

        collectEffectHelper(
            verifyEffects = {
                homeViewModel.container.sideEffect.collect {
                    println(it)
                    assertThat(it).isEqualTo(HomeSideEffect.ToggleMenu)
                }
            },
            eventsBlock = { homeViewModel.handleEvent(HomeEvent.ToggleMenu) }
        )
    }
}
