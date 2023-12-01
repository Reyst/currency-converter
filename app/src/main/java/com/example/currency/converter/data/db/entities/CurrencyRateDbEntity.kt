package com.example.currency.converter.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rates")
data class CurrencyRateDbEntity(
    @PrimaryKey
    @ColumnInfo(name = "currency_code")
    val currencyCode: String,

    @ColumnInfo(name = "rate")
    val rate: Double
)