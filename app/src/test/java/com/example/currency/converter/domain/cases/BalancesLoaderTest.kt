package com.example.currency.converter.domain.cases

import com.example.currency.converter.domain.entities.Account
import com.example.currency.converter.domain.repositories.AccountRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BalancesLoaderTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository: AccountRepository = mockk()

    private lateinit var loader: BalancesLoader

    @Before
    fun setUp() {
        loader = BalancesLoader(repository, testDispatcher)
    }

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `loader should be initialized`() = assertTrue(::loader.isInitialized)

    @Test
    fun `should return flow with balances`() = runTest {

            val balances1 = listOf(Account("EUR", 1000.0), Account("USD"))
            val balances2 = listOf(Account("EUR", 500.0), Account("USD", 600.0))

            val flow = flowOf(balances1, balances2)

            coEvery { repository.getBalances() } returns flow

            val result = loader.getBalances().toList()

            assertEquals(2, result.size)
            assertEquals(2, result.first().size)
            assertEquals(2, result.last().size)

            assertTrue(result.first().containsAll(balances1))
            assertTrue(result.last().containsAll(balances2))

            coVerify(exactly = 1) { repository.getBalances() }

            confirmVerified(repository)
        }
}