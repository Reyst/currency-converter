package com.example.currency.converter.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.currency.converter.data.db.dao.AccountDao
import com.example.currency.converter.data.db.dao.RatesDao
import com.example.currency.converter.data.db.entities.AccountDbEntity
import com.example.currency.converter.data.db.entities.CurrencyRateDbEntity


@Database(
    entities = [AccountDbEntity::class, CurrencyRateDbEntity::class],
    version = 1,
)
abstract class AppDb : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun ratesDao(): RatesDao

}

class DbCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        // add initial values
        db.execSQL("INSERT INTO accounts (currency_code,amount) VALUES('EUR', 1000)")
        db.execSQL("INSERT INTO accounts (currency_code,amount) VALUES('USD', 0)")
    }
}