package com.example.currency.converter.ui.converter

import com.example.currency.converter.domain.entities.Account

data class CurrencyConverterState(
    val balances: List<Account> = listOf(Account()),
    val availableCurrencies: List<String> = emptyList(),
    val srcAmount: Double = 0.0,
    val srcCurrency: String = "",
    val srcRate: Double = 0.0,
    val dstCurrency: String = "",
    val dstRate: Double = 0.0,
    val dstAmount: Double = 0.0,
    val fee: Double = 0.0,
    val isConversionAvailable: Boolean = false,
)