package com.example.currency.converter.domain.repositories

import com.example.currency.converter.domain.entities.ConversionRates
import kotlinx.coroutines.flow.Flow


interface RatesRepository {

    suspend fun updateRates(): Result<Map<String, Double>>

    fun getRatesForConversion(srcCurrency: String, dstCurrency: String): Flow<ConversionRates>

}