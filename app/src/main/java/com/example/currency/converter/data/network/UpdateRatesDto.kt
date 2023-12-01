package com.example.currency.converter.data.network

import com.google.gson.annotations.SerializedName

data class UpdateRatesDto(
    @SerializedName("base") val baseCurrency: String? = null,
    @SerializedName("date") val date: String? = null,
    @SerializedName("rates") val rates: Map<String, Double>? = null,
)