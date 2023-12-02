package com.example.currency.converter.di

import com.example.currency.converter.ui.converter.CurrencyConverterVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {

    viewModel { CurrencyConverterVM(get(), get()) }

}