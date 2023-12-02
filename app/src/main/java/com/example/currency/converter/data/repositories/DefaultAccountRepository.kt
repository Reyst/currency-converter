package com.example.currency.converter.data.repositories

import com.example.currency.converter.data.db.dao.AccountDao
import com.example.currency.converter.data.db.entities.AccountDbEntity
import com.example.currency.converter.domain.entities.Account
import com.example.currency.converter.domain.repositories.AccountRepository
import com.example.currency.converter.mappers.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultAccountRepository(
    private val dsLocal: LocalAccountDataSource,
): AccountRepository {
    override fun getBalances(): Flow<List<Account>> {
        return dsLocal.getAccounts()
            .map { it.toDomain() }
    }
}

interface LocalAccountDataSource {
    fun getAccounts(): Flow<List<AccountDbEntity>>
}

class RoomAccountDataSource(
    private val dao: AccountDao,
): LocalAccountDataSource {

    override fun getAccounts(): Flow<List<AccountDbEntity>> = dao.getAccounts()
}