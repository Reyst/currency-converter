package com.example.currency.converter.data.repositories

import com.example.currency.converter.data.db.dao.RatesDao
import com.example.currency.converter.data.db.entities.CurrencyRateDbEntity
import com.example.currency.converter.data.network.ExchangeRatesApi
import com.example.currency.converter.data.network.UpdateRatesDto
import com.example.currency.converter.domain.entities.ConversionRates
import com.example.currency.converter.domain.repositories.RatesRepository
import com.example.currency.converter.mappers.toDbEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultRatesRepository(
    private val dsRemote: RemoteRatesDataSource,
    private val dsLocal: LocalRatesDataSource,
) : RatesRepository {
    override suspend fun updateRates(): Result<Map<String, Double>> {
        return runCatching { dsRemote.loadRates() }
            .map { it.rates ?: emptyMap()}
            .mapCatching { response -> response.also { saveToLocalDs(it) } }
    }

    private suspend fun saveToLocalDs(rates: Map<String, Double>) {
        rates.toDbEntities()
            .also { dsLocal.saveRates(it) }
    }

    override fun getRatesForConversion(
        sellCurrency: String, receiveCurrency: String
    ): Flow<ConversionRates> {
        return dsLocal
            .getRatesFor(sellCurrency, receiveCurrency)
            .map { list ->
                ConversionRates(
                    list.firstOrNull { it.currencyCode == sellCurrency } ?.rate ?: 0.0,
                    list.firstOrNull { it.currencyCode == receiveCurrency } ?.rate ?: 0.0,
                )
            }
    }
}

interface RemoteRatesDataSource {
    suspend fun loadRates(): UpdateRatesDto
}

class RetrofitRatesDataSource(
    private val api: ExchangeRatesApi,
) : RemoteRatesDataSource {

    override suspend fun loadRates() = api.getCurrencyExchangeRates()

}

interface LocalRatesDataSource {
    suspend fun saveRates(rates: List<CurrencyRateDbEntity>)
    fun getRatesFor(vararg currencies: String): Flow<List<CurrencyRateDbEntity>>
}

class RoomRatesDataSource(
    private val dao: RatesDao,
) : LocalRatesDataSource {
    override suspend fun saveRates(rates: List<CurrencyRateDbEntity>) {
        dao.updateRates(rates)
    }

    override fun getRatesFor(vararg currencies: String): Flow<List<CurrencyRateDbEntity>> {
        return dao.getRatesFor(currencies.toSet())
    }
}