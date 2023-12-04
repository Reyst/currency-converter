package com.example.currency.converter.ui.converter

import android.icu.text.NumberFormat
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import co.wurthy.delegates.viewbinding.viewBinding
import com.example.currency.converter.R
import com.example.currency.converter.databinding.FragmentCurrencyConverterBinding
import com.example.currency.converter.domain.entities.OperationResult
import com.example.currency.converter.ui.converter.adapters.BalanceAdapter
import com.example.currency.converter.ui.converter.adapters.SelectCurrencyAdapter
import com.example.currency.converter.utils.obtainDebounceTextChangesFlow
import com.example.currency.converter.utils.subscribeAtStart
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

@OptIn(FlowPreview::class)
class CurrencyConverterFragment : Fragment(R.layout.fragment_currency_converter) {

    private val binding by viewBinding<FragmentCurrencyConverterBinding>()

    private val vm by viewModel<CurrencyConverterVM>()


    private val formatter = NumberFormat.getInstance(Locale.getDefault())
        .apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }

    private val balanceAdapter by lazy { BalanceAdapter(formatter) }

    private val sellCurrencyAdapter by lazy { SelectCurrencyAdapter(requireContext()) }

    private val receiveCurrencyAdapter by lazy { SelectCurrencyAdapter(requireContext()) }

    private var dialog: AlertDialog? = null

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

        binding.btnSubmit.setOnClickListener { vm.doConversion() }
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

        sellCurrencyAdapter.setItems(state.availableCurrencies)
        receiveCurrencyAdapter.setItems(state.availableCurrencies)

        binding.receiveAmount.text = formatter.format(state.dstAmount)
        binding.btnSubmit.isEnabled = state.isConversionAvailable && !state.inProgress

        if (state.operationResult != null) showDialog(state.operationResult)
        else dialog = null
    }

    private fun showDialog(result: OperationResult) {

        if (dialog?.isShowing == true) return

        val titleId = if (result.isError) R.string.error else R.string.success
        val message =
            if (result.isError) result.errorMessage
            else getString(
                R.string.success_message,
                formatter.format(result.srcAmount.amount),
                result.srcAmount.currency,
                formatter.format(result.dstAmount.amount),
                result.dstAmount.currency,
                formatter.format(result.fee),
            )

        dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(titleId)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ -> vm.resetResult() }
            .setCancelable(false)
            .show()
    }
}

