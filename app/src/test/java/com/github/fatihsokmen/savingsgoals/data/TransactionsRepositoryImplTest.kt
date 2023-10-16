package com.github.fatihsokmen.savingsgoals.data

import app.cash.turbine.test
import com.github.fatihsokmen.savingsgoals.data.model.AmountDto
import com.github.fatihsokmen.savingsgoals.data.model.GetTransactionsResponseDto
import com.github.fatihsokmen.savingsgoals.data.model.TransactionDto
import io.kotest.matchers.collections.shouldBeSameSizeAs
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant.Companion.parse
import org.junit.Test

class TransactionsRepositoryImplTest {

    private val transactionsApiSource: TransactionsApiService = mockk()

    @Test
    fun `GIVEN account repository WHEN accounts call made THEN it should fetch from api service`() =
        runTest {
            val transactionResponse =
                GetTransactionsResponseDto(
                    listOf(
                        TransactionDto("transaction-1-uid", AmountDto("GBP", 1000)),
                        TransactionDto("transaction-2-uid", AmountDto("GBP", 2000)),
                        TransactionDto("transaction-3-uid", AmountDto("GBP", 3000))
                    )
                )
            coEvery {
                transactionsApiSource.geTransactionsBetween(
                    "account-uid",
                    "category-uid",
                    start = "2023-10-08T13:00:00Z",
                    end = "2023-10-16T13:00:00Z"
                )
            } returns transactionResponse
            val subject = TransactionsRepositoryImpl(transactionsApiSource)

            subject.getFeed(
                "account-uid",
                "category-uid",
                start = parse("2023-10-08T13:00:00Z"),
                end = parse("2023-10-16T13:00:00Z")
            ).test {
                awaitItem() shouldBeSameSizeAs transactionResponse.feedItems
                awaitComplete()
            }
        }
}