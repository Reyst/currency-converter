package com.example.currency.converter.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

suspend inline fun <T> FlowCollector<T>.collectResult(result: Result<T>) = result.fold(
    onSuccess = { emit(it) },
    onFailure = { throw it },
)

fun <T> flowFromResult(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    resultFactory: suspend () -> Result<T>,
) = flow { collectResult(resultFactory()) }
    .flowOn(dispatcher)
