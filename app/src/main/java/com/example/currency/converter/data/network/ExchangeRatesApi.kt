package com.example.currency.converter.data.network

import retrofit2.http.GET

interface ExchangeRatesApi {
    @GET("currency-exchange-rates")
    suspend fun getCurrencyExchangeRates(): UpdateRatesDto
}

