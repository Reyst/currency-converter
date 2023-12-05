package com.example.currency.converter.data.repositories

import com.example.currency.converter.data.db.entities.CurrencyRateDbEntity
import com.example.currency.converter.data.network.UpdateRatesDto
import com.example.currency.converter.domain.repositories.RatesRepository
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class DefaultRatesRepositoryTest {

    private val dsRemote: RemoteRatesDataSource = mockk()
    private val dsLocal: LocalRatesDataSource = mockk()

    private lateinit var repository: RatesRepository

    private val apiResponse = UpdateRatesDto(
        baseCurrency = "EUR",
        date = "2024-01-01",
        rates = mapOf(
            "EUR" to 1.0,
            "USD" to 1.29,
            "UAH" to 0.5, // :)
        )
    )

    @Before
    fun setUp() {
        repository = DefaultRatesRepository(dsRemote, dsLocal)
    }

    @Test
    fun `repository should be initialized`() = assertTrue(::repository.isInitialized)


    @Test
    fun `updateRates() should get data from api and store to DB`() = runTest {

        val slotDbEntities = slot<List<CurrencyRateDbEntity>>()

        coEvery { dsRemote.loadRates() } returns apiResponse
        coJustRun { dsLocal.saveRates(capture(slotDbEntities)) }


        val result = repository.updateRates()

        assertTrue(result.isSuccess)
        assertTrue(slotDbEntities.isCaptured)

        checkDbRecords(slotDbEntities.captured)


        coVerify(exactly = 1) { dsRemote.loadRates() }
        coVerify(exactly = 1) { dsLocal.saveRates(any()) }

        confirmVerified(dsRemote, dsLocal)
    }

    @Test
    fun `updateRates() should get exception from the api and return failure`() = runTest {

        val testException = IllegalStateException("Test")
        coEvery { dsRemote.loadRates() } throws testException
        coJustRun { dsLocal.saveRates(any()) }

        val result = repository.updateRates()

        assertTrue(result.isFailure)

        assertTrue(result.exceptionOrNull() is IllegalStateException)
        assertEquals(testException.message, result.exceptionOrNull()?.message)

        coVerify(exactly = 1) { dsRemote.loadRates() }
        coVerify(exactly = 0) { dsLocal.saveRates(any()) }

        confirmVerified(dsRemote, dsLocal)
    }

    @Test
    fun `updateRates() should get exception from the local DB and return failure`() = runTest {

        val slotDbEntities = slot<List<CurrencyRateDbEntity>>()

        val testException = IllegalStateException("Test")

        coEvery { dsRemote.loadRates() } returns apiResponse
        coEvery { dsLocal.saveRates(capture(slotDbEntities)) } throws testException

        val result = repository.updateRates()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalStateException)
        assertEquals(testException.message, result.exceptionOrNull()?.message)

        assertTrue(slotDbEntities.isCaptured)
        checkDbRecords(slotDbEntities.captured)

        coVerify(exactly = 1) { dsRemote.loadRates() }
        coVerify(exactly = 1) { dsLocal.saveRates(any()) }

        confirmVerified(dsRemote, dsLocal)
    }

    private fun checkDbRecords(dbRecords: List<CurrencyRateDbEntity>) {
        assertEquals(3, dbRecords.size)

        dbRecords.firstOrNull { it.currencyCode == "EUR" }
            ?.also { assertEquals(1.0, it.rate, 0.001) }
            ?: fail("record 'EUR' is absent")
        dbRecords.firstOrNull { it.currencyCode == "USD" }
            ?.also { assertEquals(1.29, it.rate, 0.001) }
            ?: fail("record 'USD' is absent")
        dbRecords.firstOrNull { it.currencyCode == "UAH" }
            ?.also { assertEquals(0.5, it.rate, 0.001) }
            ?: fail("record 'UAH' is absent")
    }
}