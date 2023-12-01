package com.example.currency.converter.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currency.converter.data.db.dao.AccountDao
import com.example.currency.converter.data.db.dao.OperationsDao
import com.example.currency.converter.data.db.dao.RatesDao
import com.example.currency.converter.data.db.entities.AccountDbEntity
import com.example.currency.converter.data.db.entities.CurrencyRateDbEntity


@Database(entities = [AccountDbEntity::class, CurrencyRateDbEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun ratesDao(): RatesDao
    abstract fun operationsDao(): OperationsDao
}

