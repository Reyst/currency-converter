package com.example.currency.converter.ui.converter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currency.converter.domain.cases.BalancesLoader
import com.example.currency.converter.domain.cases.ConversionExecutor
import com.example.currency.converter.domain.cases.ConversionValidator
import com.example.currency.converter.domain.cases.FeeProvider
import com.example.currency.converter.domain.cases.RatesProvider
import com.example.currency.converter.domain.cases.RatesUpdater
import com.example.currency.converter.domain.entities.OperationAmount
import com.example.currency.converter.domain.entities.OperationResult
import com.github.reyst.utils.invokeEach
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class CurrencyConverterVM(
    private val balanceLoader: BalancesLoader,
    private val ratesUpdater: RatesUpdater,
    private val ratesProvider: RatesProvider,
    private val feeProvider: FeeProvider,
    private val validator: ConversionValidator,
    private val conversionExecutor: ConversionExecutor,
) : ViewModel() {

    private val balancesMap = mutableMapOf<String, Double>()
    private val vmScope: CoroutineScope
        get() = viewModelScope

    private val _state = MutableStateFlow(CurrencyConverterState())

    val state = _state.asStateFlow()

    private var rateFlowJob: Job? = null

    fun init() {

        vmScope.launch {
            balanceLoader
                .getBalances()
                .onEach {
                    it.map { (currency, rest) -> currency to rest }
                        .also(balancesMap::putAll)
                }
                .collect { balances ->
                    _state.update { oldState ->
                        oldState.copy(
                            balances = balances,
                            availableCurrencies = balances.map { it.currency },
                        )
                    }
                }
        }

        vmScope.invokeEach(
            interval = 5.seconds,
            Dispatchers.IO,
        ) {
            ratesUpdater.updateRates()
                .catch { emit(emptyMap()) }
                .collect()
        }
    }

    fun updateSellingAmount(amountStr: CharSequence) {
        val amount = amountStr.toString().toDoubleOrNull() ?: 0.0

        _state.update {
            it.copy(
                srcAmount = amount,
                dstAmount = calculateReceiveAmount(amount, it.srcRate, it.dstRate),
            ).checkOperation()
        }
    }

    fun updateSellCurrency(currency: String) {
        Log.wtf("INSPECT", "updateSellCurrency: $currency")
        _state.update { it.copy(srcCurrency = currency) }
        requestRatesForConversion(currency, state.value.dstCurrency)
    }

    fun updateReceiveCurrency(currency: String) {
        Log.wtf("INSPECT", "updateReceiveCurrency: $currency")
        _state.update { it.copy(dstCurrency = currency) }
        requestRatesForConversion(state.value.srcCurrency, currency)
    }

    private fun requestRatesForConversion(srcCurrency: String, dstCurrency: String) {
        rateFlowJob?.cancel()
        rateFlowJob = vmScope.launch {
            ratesProvider.getConversionRates(srcCurrency, dstCurrency)
                .catch { _state.update { it.copy(isConversionAvailable = false) } }
                .collect { (srcRate, dstRate) ->
                    _state.update {
                        it.copy(
                            srcRate = srcRate,
                            dstRate = dstRate,
                            dstAmount = calculateReceiveAmount(it.srcAmount, srcRate, dstRate),
                            fee = feeProvider.getFee(srcRate),
                        ).checkOperation()
                    }
                }
        }
    }

    private fun CurrencyConverterState.checkOperation(): CurrencyConverterState {
        return copy(
            isConversionAvailable = validator.isOperationValid(
                balancesMap[srcCurrency] ?: 0.0,
                OperationAmount(srcAmount, srcCurrency, srcRate),
                OperationAmount(dstAmount, dstCurrency, dstRate),
                fee,
            )
        )
    }

    private fun calculateReceiveAmount(
        srcAmount: Double,
        srcRate: Double,
        dstRate: Double
    ): Double {
        return try {
            srcAmount * dstRate / srcRate
        } catch (t: Throwable) {
            0.0
        }
    }

    fun doConversion() {

        _state.update { it.copy(inProgress = true) }
        vmScope.launch {

            val currentState = state.value
            conversionExecutor
                .convert(
                    OperationAmount(
                        currentState.srcAmount,
                        currentState.srcCurrency,
                        currentState.srcRate,
                    ),
                    OperationAmount(
                        currentState.dstAmount,
                        currentState.dstCurrency,
                        currentState.dstRate,
                    ),
                    currentState.fee,
                )
                .catch { emit(OperationResult(isError = true, errorMessage = it.message)) }
                .collect { result -> _state.update { it.copy(operationResult = result) } }
        }
    }

    fun resetResult() {
        _state.update { it.copy(inProgress = false, operationResult = null) }
    }
}