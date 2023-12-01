package com.example.currency.converter.ui.converter

import android.icu.text.NumberFormat
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import co.wurthy.delegates.viewbinding.viewBinding
import com.example.currency.converter.R
import com.example.currency.converter.databinding.FragmentCurrencyConverterBinding
import com.example.currency.converter.ui.converter.adapters.BalanceAdapter
import com.example.currency.converter.ui.converter.adapters.SelectCurrencyAdapter
import com.example.currency.converter.utils.obtainDebounceTextChangesFlow
import com.example.currency.converter.utils.subscribeAtStart
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

@OptIn(FlowPreview::class)
class CurrencyConverterFragment : Fragment(R.layout.fragment_currency_converter) {

    private val binding by viewBinding<FragmentCurrencyConverterBinding>()

    private val vm by viewModel<CurrencyConverterVM>()

    private val balanceAdapter by lazy {
        val formatter = NumberFormat.getInstance(Locale.getDefault()).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
        BalanceAdapter(formatter)
    }

    private val sellCurrencyAdapter by lazy { SelectCurrencyAdapter(requireContext()) }

    private val receiveCurrencyAdapter by lazy { SelectCurrencyAdapter(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeAtStart(vm.state, ::render)

        if (savedInstanceState == null) vm.init()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sellCurrency.onItemSelectedListener =
            getCurrencySelectedListener(vm::updateSellCurrency)
        binding.receiveCurrency.onItemSelectedListener =
            getCurrencySelectedListener(vm::updateReceiveCurrency)

        binding.rvBalances.adapter = balanceAdapter
        binding.sellCurrency.adapter = sellCurrencyAdapter
        binding.receiveCurrency.adapter = receiveCurrencyAdapter

        binding.sellAmount.obtainDebounceTextChangesFlow()
            .onEach { vm.updateSellingAmount(it) }
            .launchIn(lifecycleScope)
    }

    private fun getCurrencySelectedListener(action: (String) -> Unit): OnItemSelectedListener {
        return object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                parent
                    ?.adapter
                    ?.getItem(position)
                    ?.let { it as String }
                    ?.also(action)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    private fun render(state: CurrencyConverterState) {

        balanceAdapter.submitList(state.balances)

        state.balances
            .map { it.currency }
            .also {
                sellCurrencyAdapter.setItems(it)
                receiveCurrencyAdapter.setItems(it)
            }

        binding.btnSubmit.isEnabled = state.isConversionAvailable
    }
}

