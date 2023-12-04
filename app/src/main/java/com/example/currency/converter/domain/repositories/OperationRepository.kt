package com.example.currency.converter.domain.repositories

import com.example.currency.converter.domain.entities.OperationAmount
import com.example.currency.converter.domain.entities.OperationResult

interface OperationRepository {

    suspend fun convert(src: OperationAmount, dst: OperationAmount, fee: Double): Result<OperationResult>

}