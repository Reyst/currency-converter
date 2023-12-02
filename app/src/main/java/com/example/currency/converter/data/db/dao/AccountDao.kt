package com.example.currency.converter.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.currency.converter.data.db.entities.AccountDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM accounts")
    fun getAccounts(): Flow<List<AccountDbEntity>>

}

