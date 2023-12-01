package com.example.currency.converter.di

import com.example.currency.converter.data.network.ExchangeRatesApi
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    factory<ExchangeRatesApi> { get<Retrofit>().create(ExchangeRatesApi::class.java) }
}