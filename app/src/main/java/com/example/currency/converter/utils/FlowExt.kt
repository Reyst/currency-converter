package com.example.currency.converter.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

suspend inline fun <T> FlowCollector<T>.collectResult(result: Result<T>) = result.fold(
    onSuccess = { emit(it) },
    onFailure = { throw it },
)

fun <T> flowFromResult(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    resultFactory: suspend () -> Result<T>,
) = flow { collectResult(resultFactory()) }
    .flowOn(dispatcher)


fun <T> LifecycleOwner.subscribeAtStart(flow: SharedFlow<T>, collector: FlowCollector<T>): Job {
    return subscribeAt(
        Lifecycle.State.STARTED,
        flow,
        collector,
    )
}

fun <T> LifecycleOwner.subscribeAtResume(flow: SharedFlow<T>, collector: FlowCollector<T>): Job {
    return subscribeAt(
        Lifecycle.State.RESUMED,
        flow,
        collector,
    )
}


fun <T> LifecycleOwner.subscribeAt(
    state: Lifecycle.State,
    flow: SharedFlow<T>,
    collector: FlowCollector<T>,
): Job = lifecycleScope.launch {
    repeatOnLifecycle(state) { flow.collect(collector) }
}
