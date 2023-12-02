package com.example.currency.converter.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

fun CoroutineScope.invokeEach(
    interval: Duration = Duration.ZERO,
    context: CoroutineContext = this.coroutineContext,
    block: suspend () -> Unit
): Job = launch(context) {
    try {
        do {
            block()
            delay(interval)
        } while (interval > Duration.ZERO && isActive)
    } catch (_: Exception) {
    }
}