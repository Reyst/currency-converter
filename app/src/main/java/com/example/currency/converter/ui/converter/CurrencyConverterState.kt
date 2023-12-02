package com.example.currency.converter.ui.converter

import com.example.currency.converter.domain.entities.Account

data class CurrencyConverterState(
    val balances: List<Account> = listOf(Account()),
    val rates: Map<String, Double> = emptyMap(),
    val sell: Double = 0.0,
    val receive: Double = 0.0,
    val isConversionAvailable: Boolean = false,
    val sellCurrency: String = "",
    val receiveCurrency: String = "",
)