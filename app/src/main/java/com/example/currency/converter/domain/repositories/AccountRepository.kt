package com.example.currency.converter.domain.repositories

import com.example.currency.converter.domain.entities.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    fun getBalances(): Flow<List<Account>>

}