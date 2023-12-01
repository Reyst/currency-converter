package com.example.currency.converter.ui.converter.adapters

import android.content.Context
import android.widget.ArrayAdapter
import com.example.currency.converter.R

class SelectCurrencyAdapter(
    context: Context,
) : ArrayAdapter<String>(context, R.layout.currency_item) {

    fun setItems(items: List<String>) {

        clear()
        addAll(items)
        notifyDataSetChanged()

//        if (count != items.size) {
//
//        } else if (items.any { getPosition(it) == -1 }) {
//
//        }
    }

}