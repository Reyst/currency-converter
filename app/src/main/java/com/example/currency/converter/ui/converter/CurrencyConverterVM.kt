package com.example.currency.converter.ui.converter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currency.converter.domain.cases.BalancesLoader
import com.example.currency.converter.domain.cases.RatesUpdater
import com.example.currency.converter.utils.invokeEach
import com.example.currency.converter.utils.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CurrencyConverterVM(
    private val balanceLoader: BalancesLoader,
    private val ratesUpdater: RatesUpdater,
): ViewModel() {

    private val vmScope: CoroutineScope
        get() = viewModelScope

    private val _state = MutableStateFlow(CurrencyConverterState())

    val state = _state.asStateFlow()

    fun init() {

        vmScope.launch {
            balanceLoader
                .getBalances()
                .collect { balances -> _state.update { it.copy(balances = balances) } }
        }

        vmScope.invokeEach(
            interval = 5.seconds(),
            Dispatchers.IO,
        ) {
            ratesUpdater.updateRates()
                .catch { emit(emptyMap()) }
                .collect()
        }
    }

    fun updateSellingAmount(amountStr: CharSequence) {

    }

    fun updateSellCurrency(currency: String) {
        Log.wtf("INSPECT", "updateSellCurrency: $currency")
        _state.update { it.copy(sellCurrency = currency) }
    }

    fun updateReceiveCurrency(currency: String) {
        Log.wtf("INSPECT", "updateReceiveCurrency: $currency")
        _state.update { it.copy(receiveCurrency = currency) }
    }



}