package com.example.currency.converter.domain.cases

import com.example.currency.converter.domain.entities.OperationAmount
import com.example.currency.converter.domain.repositories.OperationRepository
import com.github.reyst.utils.flowFromResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ConversionExecutor(
    private val repository: OperationRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    fun convert(src: OperationAmount, dst:OperationAmount, fee: Double) = flowFromResult(dispatcher) {
        repository.convert(src, dst, fee)
    }

}