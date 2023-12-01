package com.example.currency.converter.ui.converter.adapters

import android.icu.text.NumberFormat
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.currency.converter.R
import com.example.currency.converter.domain.entities.Account
import com.example.currency.converter.utils.inflate

class BalanceAdapter(
    private val formatter: NumberFormat,
) : ListAdapter<Account, AccountVH>(obtainDiffer()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountVH {
        return AccountVH(parent.inflate(R.layout.balance_item))
    }

    override fun onBindViewHolder(holder: AccountVH, position: Int) {
        getItem(position)
            .also {
                holder.binding.currency.text = it.currency
                holder.binding.amount.text = formatter.format(it.amount)
            }
    }

    companion object {
        fun obtainDiffer() = object : DiffUtil.ItemCallback<Account>() {
            override fun areItemsTheSame(
                oldItem: Account,
                newItem: Account,
            ): Boolean = oldItem.currency == newItem.currency

            override fun areContentsTheSame(
                oldItem: Account,
                newItem: Account,
            ): Boolean = oldItem == newItem
        }
    }
}