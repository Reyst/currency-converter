package com.example.currency.converter.data.repositories

import com.example.currency.converter.data.db.dao.AccountDao
import com.example.currency.converter.domain.entities.OperationAmount
import com.example.currency.converter.domain.entities.OperationResult
import com.example.currency.converter.domain.repositories.OperationRepository

class DefaultOperationRepository(
    private val dsLocal: LocalOperationDataSource,
) : OperationRepository {
    override suspend fun convert(
        src: OperationAmount,
        dst: OperationAmount,
        fee: Double
    ): Result<OperationResult> {
        return runCatching {
            dsLocal.convert(
                src.amount, src.currency,
                dst.amount, dst.currency,
                fee,
            )

            OperationResult(src, dst, fee)
        }
    }
}

interface LocalOperationDataSource {
    suspend fun convert(
        srcAmount: Double,
        srcCurrency: String,
        dstAmount: Double,
        dstCurrency: String,
        fee: Double,
    )
}

class RoomOperationDataSource(
    private val dao: AccountDao
):LocalOperationDataSource {
    override suspend fun convert(
        srcAmount: Double,
        srcCurrency: String,
        dstAmount: Double,
        dstCurrency: String,
        fee: Double
    ) = dao.move(srcAmount, srcCurrency, dstAmount, dstCurrency, fee)
}