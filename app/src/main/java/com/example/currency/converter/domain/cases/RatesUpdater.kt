package com.example.currency.converter.domain.cases

import com.example.currency.converter.domain.repositories.RatesRepository
import com.github.reyst.utils.flowFromResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RatesUpdater(
    private val repository: RatesRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    fun updateRates() = flowFromResult(dispatcher) { repository.updateRates() }
}