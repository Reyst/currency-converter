package com.example.currency.converter.domain.cases

class FeeProvider {

    fun getFee(rate: Double): Double {
        return FEE * rate
    }

    companion object {
        private const val FEE = 0.7
    }

}