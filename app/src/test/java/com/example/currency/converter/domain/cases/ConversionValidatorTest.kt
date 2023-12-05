package com.example.currency.converter.domain.cases

import com.example.currency.converter.domain.entities.OperationAmount
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class ConversionValidatorTest {

    private lateinit var validator: ConversionValidator


    @Before
    fun setUp() {
        validator = ConversionValidator()
    }

    @Test
    fun `validator should be initialized`() = assertTrue(::validator.isInitialized)


    @Suppress("JUnitMalformedDeclaration")
    @Parameters(method = "getConversionParams")
    @Test
    fun `should check params and return correct result`(
        rest: Double,
        src: OperationAmount,
        dst: OperationAmount,
        fee: Double,
        expectedResult: Boolean,
    ) = assertEquals(expectedResult, validator.isOperationValid(rest, src, dst, fee))


    @Suppress("unused")
    @Parameters
    private fun getConversionParams() = arrayOf(
        arrayOf( // the same currencies - false
            100.0,
            OperationAmount(10.0, "EUR", 1.0),
            OperationAmount(20.0, "EUR", 1.0),
            0.0,
            false,
        ),
        arrayOf( // src amount = 0 - false
            100.0,
            OperationAmount(0.0, "EUR", 1.0),
            OperationAmount(10.0, "USD", 1.0),
            1.0,
            false,
        ),
        arrayOf( // dst amount = 0 - false
            100.0,
            OperationAmount(10.0, "EUR", 1.0),
            OperationAmount(0.0, "USD", 1.0),
            0.0,
            false,
        ),
        arrayOf( // insufficient funds = 0 - false
            100.0,
            OperationAmount(100.0, "EUR", 1.0),
            OperationAmount(100.0, "USD", 1.0),
            1.0,
            false,
        ),
        arrayOf( // src rate = 0 - false
            100.0,
            OperationAmount(10.0, "EUR", 0.0),
            OperationAmount(10.0, "USD", 1.0),
            1.0,
            false,
        ),
        arrayOf( // dst rate = 0 - false
            100.0,
            OperationAmount(10.0, "EUR", 1.0),
            OperationAmount(10.0, "USD", 0.0),
            1.0,
            false,
        ),
        arrayOf( // true
            100.0,
            OperationAmount(10.0, "EUR", 1.0),
            OperationAmount(10.0, "USD", 1.0),
            1.0,
            true,
        ),
    )
}