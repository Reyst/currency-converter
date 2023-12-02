package com.example.currency.converter.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currency.converter.data.db.entities.CurrencyRateDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RatesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRates(rates: List<CurrencyRateDbEntity>)

    @Query("SELECT * FROM rates WHERE currency_code in ((:currencies))")
    fun getRatesFor(currencies: Set<String>): Flow<List<CurrencyRateDbEntity>>
}
