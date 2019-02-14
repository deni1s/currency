package com.example.currency.utils.coroutines

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.launch

object EventBus {
    val bus: BroadcastChannel<Any> = BroadcastChannel<Any>(Channel.CONFLATED)

    fun send(o: Any) {
        GlobalScope.launch {
            bus.send(o)
        }
    }

    inline fun <reified T> asChannel(): ReceiveChannel<T> {
        return bus.openSubscription().filter { it is T }.map  { it as T }
    }
}