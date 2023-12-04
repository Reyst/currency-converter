package com.example.currency.converter.domain.cases

import com.example.currency.converter.domain.entities.ConversionRates
import com.example.currency.converter.domain.repositories.RatesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class RatesProvider(
    private val repository: RatesRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    fun getConversionRates(srcCurrency: String, dstCurrency: String): Flow<ConversionRates> {
        return repository.getRatesForConversion(srcCurrency, dstCurrency)
            .flowOn(dispatcher)
    }
}