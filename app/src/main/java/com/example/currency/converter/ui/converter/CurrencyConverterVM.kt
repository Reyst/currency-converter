package com.example.currency.converter.ui.converter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CurrencyConverterVM(


): ViewModel() {

    private val vmScope: CoroutineScope
        get() = viewModelScope

    private val _state = MutableStateFlow(CurrencyConverterState())

    val state = _state.asStateFlow()

    fun init() {


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