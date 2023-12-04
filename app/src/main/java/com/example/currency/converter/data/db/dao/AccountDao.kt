package com.example.currency.converter.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.currency.converter.data.db.entities.AccountDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AccountDao {

    @Query("SELECT * FROM accounts")
    abstract fun getAccounts(): Flow<List<AccountDbEntity>>

    @Query("SELECT * FROM accounts WHERE currency_code = :currency LIMIT 1")
    abstract suspend fun getAccount(currency: String): AccountDbEntity

    @Update
    abstract suspend fun update(account: AccountDbEntity)

    @Transaction
    open suspend fun move(
        srcAmount: Double,
        srcCurrency: String,
        dstAmount: Double,
        dstCurrency: String,
        fee: Double,
    ) {
        getAccount(srcCurrency)
            .let { it.copy(amount = it.amount - srcAmount - fee) }
            .takeIf { it.amount >= 0 }
            ?.also { update(it) }
            ?: throw IllegalStateException("Insufficient funds")

        getAccount(dstCurrency)
            .let { it.copy(amount = it.amount + dstAmount) }
            .takeIf { it.amount >= 0 }
            ?.also { update(it) }
            ?: throw IllegalStateException("Incorrect rates")
    }
}

