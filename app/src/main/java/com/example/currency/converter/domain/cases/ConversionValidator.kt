package com.example.currency.converter.domain.cases

import com.example.currency.converter.domain.entities.OperationAmount

class ConversionValidator {
    fun isOperationValid(
        rest: Double,
        srcAmount: OperationAmount,
        dstAmount: OperationAmount,
        fee: Double,
    ): Boolean {
        return rest >= srcAmount.amount + fee
                && srcAmount.currency != dstAmount.currency
                && srcAmount.amount > 0.0
                && dstAmount.amount > 0.0
                && srcAmount.rate > 0.0
                && dstAmount.rate > 0.0
    }
}