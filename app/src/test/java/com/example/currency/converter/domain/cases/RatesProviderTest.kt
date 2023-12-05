package com.example.currency.converter.domain.cases

import com.example.currency.converter.domain.entities.ConversionRates
import com.example.currency.converter.domain.repositories.RatesRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RatesProviderTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository: RatesRepository = mockk()

    private lateinit var provider: RatesProvider

    private val currency1 = "EUR"
    private val currency2 = "USD"


    @Before
    fun setUp() {
        provider = RatesProvider(repository, testDispatcher)
    }

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `provider should be initialized`() = assertTrue(::provider.isInitialized)

    @Test
    fun `should return flow with conversion rates`() = runTest {

        val flow = flowOf(ConversionRates(1.0, 2.0))
        every { repository.getRatesForConversion(any(), any()) } returns flow


        val result = provider.getConversionRates(currency1, currency2).toList()

        assertEquals(1, result.size)

        val rates = result.firstOrNull()
        assertEquals(1.0, rates?.sellRate)
        assertEquals(2.0, rates?.receiveRate)

        verify(exactly = 1) { repository.getRatesForConversion(eq(currency1), eq(currency2)) }

        confirmVerified(repository)
    }

    @Test
    fun `should return flow with exception`() = runTest {

        val testException = IllegalStateException("test exception")
        val flow = flow<ConversionRates> { throw testException }

        every { repository.getRatesForConversion(any(), any()) } returns flow


        val result = mutableListOf<Throwable>()
        provider.getConversionRates(currency1, currency2)
            .catch { result.add(it) }
            .collect { Assert.fail("Shouldn't be collected") }

        assertEquals(1, result.size)
        assertEquals(testException.message, result.firstOrNull()?.message)
        assertTrue(result.firstOrNull() is IllegalStateException)
        verify(exactly = 1) { repository.getRatesForConversion(eq(currency1), eq(currency2)) }

        confirmVerified(repository)
    }
}