package com.example.currency.converter.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.currency.converter.data.db.entities.CurrencyRateDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RatesDao {
    @Upsert
    suspend fun updateRates(rates: List<CurrencyRateDbEntity>)

    @Query("SELECT * FROM rates WHERE currency_code in (:currencies)")
    fun getRatesFor(vararg currencies: String): Flow<List<CurrencyRateDbEntity>>
}
