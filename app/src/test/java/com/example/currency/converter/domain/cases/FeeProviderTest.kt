package com.example.currency.converter.domain.cases

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class FeeProviderTest {

    private lateinit var provider: FeeProvider

    @Before
    fun setUp() {
        provider = FeeProvider()
    }

    @Test
    fun `provider should be initialized`() = assertTrue(::provider.isInitialized)


    @Suppress("JUnitMalformedDeclaration")
    @Parameters(method = "getFees")
    @Test
    fun `should return 0,70 eur multiplied to rate`(rate: Double, resultFee: Double) {
        assertEquals(resultFee, provider.getFee(rate), 0.01)
    }

    @Suppress("unused")
    @Parameters
    private fun getFees() = arrayOf(
        arrayOf(1.0, STATIC_FEE),
        arrayOf(10.0, 10 * STATIC_FEE),
        arrayOf(0.1, 0.1 * STATIC_FEE),
        arrayOf(0.0, 0.0),
    )

    companion object {
        private const val STATIC_FEE = 0.7
    }

}