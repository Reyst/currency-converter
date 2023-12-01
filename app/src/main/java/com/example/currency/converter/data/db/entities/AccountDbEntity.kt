package com.example.currency.converter.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountDbEntity(
    @PrimaryKey
    @ColumnInfo(name = "currency_code")
    val currencyCode: String,

    @ColumnInfo(name = "amount", defaultValue = "0.00", typeAffinity = ColumnInfo.REAL)
    val amount: Double
)
