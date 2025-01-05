package com.bruno13palhano.data.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

class Container<STATE, EFFECT>(
    initialState: STATE,
    private val scope: CoroutineScope
) {
    private val _state: MutableStateFlow<STATE> = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state.asStateFlow()

    private val _sideEffect = Channel<EFFECT>(Channel.CONFLATED)
    val sideEffect: Flow<EFFECT> = _sideEffect.receiveAsFlow()

    fun intent(transform: suspend Container<STATE, EFFECT>.() -> Unit) {
        scope.launch(SINGLE_THREAD) {
            this@Container.transform()
        }
    }

    suspend fun reduce(reducer: suspend STATE.() -> STATE) {
        withContext(SINGLE_THREAD) {
            _state.value = _state.value.reducer()
        }
    }

    suspend fun postSideEffect(effect: EFFECT) {
        _sideEffect.send(effect)
    }

    companion object {
        @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
        private val SINGLE_THREAD = newSingleThreadContext("SingleThread")
    }
}