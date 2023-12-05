package com.example.currency.converter.domain.cases

import com.example.currency.converter.domain.repositories.RatesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RatesUpdaterTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository: RatesRepository = mockk()

    private lateinit var updater: RatesUpdater

    @Before
    fun setUp() {
        updater = RatesUpdater(repository, testDispatcher)
    }

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `updater should be initialized`() = assertTrue(::updater.isInitialized)


    @Test
    fun `should update rates successfully`() = runTest {

        val rates = mapOf("EUR" to 1.0, "USD" to 1.3, "UAH" to 0.5) // :)
        coEvery { repository.updateRates() } returns Result.success(rates)


        val result = updater.updateRates().toList()

        assertEquals(1, result.size)
        assertEquals(rates, result.firstOrNull())

        coVerify(exactly = 1) { repository.updateRates() }
        confirmVerified(repository)
    }

    @Test
    fun `should return flow with exception`() = runTest {

        val testException = IllegalStateException("Test exception")
        coEvery { repository.updateRates() } returns Result.failure(testException)


        val result = mutableListOf<Throwable>()

        updater.updateRates()
            .catch { result.add(it) }
            .collect { fail("Shouldn't be collected") }

        assertEquals(1, result.size)
        assertEquals(testException.message, result.firstOrNull()?.message)
        assertTrue(result.firstOrNull() is IllegalStateException)

        coVerify(exactly = 1) { repository.updateRates() }
        confirmVerified(repository)
    }
}