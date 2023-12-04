package com.example.currency.converter.domain.entities

data class OperationResult(
    val srcAmount: OperationAmount = OperationAmount(),
    val dstAmount: OperationAmount = OperationAmount(),
    val fee: Double = 0.0,
    val isError: Boolean = false,
    val errorMessage: String? = null,
)
