package com.github.fatihsokmen.savingsgoals.domain

import app.cash.turbine.test
import com.github.fatihsokmen.savingsgoals.data.AccountsRepository
import com.github.fatihsokmen.savingsgoals.data.TransactionsRepository
import com.github.fatihsokmen.savingsgoals.data.model.AccountDto
import com.github.fatihsokmen.savingsgoals.data.model.AmountDto
import com.github.fatihsokmen.savingsgoals.data.model.TransactionDto
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.math.BigDecimal

@RunWith(Parameterized::class)
class CalculateRoundupUseCaseTest(
    private val givenTransactions: List<TransactionDto>,
    private val expected: BigDecimal
) {
    private val accountsRepository: AccountsRepository = mockk(relaxed = true)
    private val transactionsRepository: TransactionsRepository = mockk(relaxed = true)

    @Test
    fun `GIVEN use case WHEN executed THEN should produce correct round up`() = runTest {
        val account = AccountDto("account-uid", "account-category")
        every {
            accountsRepository.getAccounts()
        } returns flowOf(listOf(account))

        every {
            transactionsRepository.getFeed(
                "account-uid",
                "account-category",
                Instant.DISTANT_PAST,
                Instant.DISTANT_FUTURE
            )
        } returns flowOf(givenTransactions)

        val dispatcher = StandardTestDispatcher(testScheduler)
        val subject =
            CalculateRoundupUseCase(accountsRepository, transactionsRepository, dispatcher)

        subject.execute(Instant.DISTANT_PAST, Instant.DISTANT_FUTURE).test {
            awaitItem() shouldBe expected
            awaitComplete()
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(
                    listOf(
                        TransactionDto("transaction-uid1", AmountDto("GBP", 120)),
                        TransactionDto("transaction-uid2", AmountDto("GBP", 140)),
                        TransactionDto("transaction-uid2", AmountDto("GBP", 160)),
                    ), BigDecimal("1.8")
                ),
                arrayOf(
                    listOf(
                        TransactionDto("transaction-uid1", AmountDto("GBP", 328)),
                        TransactionDto("transaction-uid2", AmountDto("GBP", 547)),
                        TransactionDto("transaction-uid2", AmountDto("GBP", 63)),
                    ), BigDecimal("1.62")
                )
            )
        }
    }
}