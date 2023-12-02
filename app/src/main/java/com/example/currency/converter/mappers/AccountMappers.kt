package com.example.currency.converter.mappers

import com.example.currency.converter.data.db.entities.AccountDbEntity
import com.example.currency.converter.domain.entities.Account

fun AccountDbEntity.toDomain() = Account(currencyCode, amount)
fun List<AccountDbEntity>.toDomain() = map { it.toDomain() }