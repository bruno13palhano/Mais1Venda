package com.bruno13palhano.data.notifications

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Singleton
class EventBus @Inject constructor() {
    private val _events =
        MutableSharedFlow<OrderEvent>(replay = 5, extraBufferCapacity = Channel.UNLIMITED)
    val events = _events.asSharedFlow()

    suspend fun publish(event: OrderEvent) {
        _events.emit(event)
    }
}
