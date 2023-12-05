package com.example.currency.converter.domain.cases

import com.example.currency.converter.domain.entities.OperationAmount
import com.example.currency.converter.domain.entities.OperationResult
import com.example.currency.converter.domain.repositories.OperationRepository
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
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConversionExecutorTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository: OperationRepository = mockk()

    private val src = OperationAmount(10.0, "EUR", 1.0)
    private val dst = OperationAmount(10.0, "USD", 1.0)
    private val fee = 1.0


    private lateinit var executor: ConversionExecutor

    @Before
    fun setUp() {
        executor = ConversionExecutor(repository, testDispatcher)
    }

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `executor should be initialized`() = assertTrue(::executor.isInitialized)

    @Test
    fun `executor should return flow with successful operation result`() = runTest {

        coEvery { repository.convert(any(), any(), any()) } returns Result.success(
            OperationResult(src, dst, fee)
        )


        val result = executor.convert(src, dst, fee).toList()

        assertEquals(1, result.size)

        val operationResult = result.firstOrNull()
        assertEquals(false, operationResult?.isError)
        assertEquals(src, operationResult?.srcAmount)
        assertEquals(dst, operationResult?.dstAmount)
        assertEquals(fee, operationResult?.fee ?: 0.0, 0.01)

        coVerify(exactly = 1) { repository.convert(eq(src), eq(dst), eq(fee)) }
        confirmVerified(repository)
    }

    @Test
    fun `executor should return flow with exception`() = runTest {

        val testException = IllegalStateException("Test exception")

        coEvery { repository.convert(any(), any(), any()) } returns Result.failure(testException)

        val result = mutableListOf<Throwable>()
        executor.convert(src, dst, fee)
            .catch { result.add(it) }
            .collect { Assert.fail("Shouldn't be collected") }

        assertEquals(1, result.size)
        assertEquals(testException.message, result.firstOrNull()?.message)
        assertTrue(result.firstOrNull() is IllegalStateException)

        coVerify(exactly = 1) { repository.convert(eq(src), eq(dst), eq(fee)) }
        confirmVerified(repository)
    }
}