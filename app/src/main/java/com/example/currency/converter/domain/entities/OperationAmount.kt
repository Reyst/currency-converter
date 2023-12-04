package com.example.currency.converter.domain.entities

data class OperationAmount(
    val amount: Double = 0.0,
    val currency: String = "",
    val rate: Double = 0.0,
)