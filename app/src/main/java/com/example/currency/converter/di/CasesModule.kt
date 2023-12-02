package com.example.currency.converter.di

import com.example.currency.converter.domain.cases.BalancesLoader
import com.example.currency.converter.domain.cases.RatesUpdater
import org.koin.dsl.module

val casesModule = module {

    factory { BalancesLoader(get()) }
    factory { RatesUpdater(get()) }

}