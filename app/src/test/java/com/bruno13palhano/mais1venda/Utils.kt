package com.bruno13palhano.mais1venda

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle

@OptIn(ExperimentalCoroutinesApi::class)
fun TestScope.collectEffectHelper(verifyEffects: suspend () -> Unit, eventsBlock: () -> Unit) {
    val collectJob = launch { verifyEffects() }

    eventsBlock()
    advanceUntilIdle()

    collectJob.cancel()
}

@OptIn(ExperimentalCoroutinesApi::class)
fun TestScope.collectStateHelper(
    stateCollector: suspend () -> Unit,
    eventsBlock: () -> Unit,
    assertationsBlock: () -> Unit,
) {
    val collectJob = launch { stateCollector() }

    eventsBlock()

    advanceUntilIdle()
    assertationsBlock()

    collectJob.cancel()
}
