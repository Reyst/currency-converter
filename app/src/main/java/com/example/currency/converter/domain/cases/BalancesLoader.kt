package com.example.currency.converter.domain.cases

import com.example.currency.converter.domain.entities.Account
import com.example.currency.converter.domain.repositories.AccountRepository
import kotlinx.coroutines.flow.Flow

class BalancesLoader(
    private val repository: AccountRepository,
) {

    fun getBalances(): Flow<List<Account>> = repository.getBalances()
}