package com.example.currency.converter.domain.cases

import com.example.currency.converter.domain.entities.Account
import com.example.currency.converter.domain.repositories.AccountRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class BalancesLoader(
    private val repository: AccountRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    fun getBalances(): Flow<List<Account>> = repository.getBalances()
        .flowOn(dispatcher)
}