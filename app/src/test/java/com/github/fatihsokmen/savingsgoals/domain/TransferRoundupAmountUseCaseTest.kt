package com.github.fatihsokmen.savingsgoals.domain

import app.cash.turbine.test
import com.github.fatihsokmen.savingsgoals.data.AccountsRepository
import com.github.fatihsokmen.savingsgoals.data.SavingsGoalsRepository
import com.github.fatihsokmen.savingsgoals.data.model.AccountDto
import com.github.fatihsokmen.savingsgoals.data.model.TransferIntoGoalResponseDto
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.math.BigDecimal

class TransferRoundupAmountUseCaseTest {
    private val accountsRepository: AccountsRepository = mockk(relaxed = true)
    private val savingsGoalsRepository: SavingsGoalsRepository = mockk(relaxed = true)

    @Test
    fun `GIVEN use case WHEN executed THEN should transfer amount into goal`() = runTest {
        val account = AccountDto("account-uid", "account-category")
        every {
            accountsRepository.getAccounts()
        } returns flowOf(listOf(account))

        val transferResponse = TransferIntoGoalResponseDto
        coEvery {
            savingsGoalsRepository.addIntoSavingsGoal(
                accountUid = "account-uid",
                savingsGoalUid = "saving-goal-uid",
                transferUid = any(),
                amount = BigDecimal.TEN
            )
        } returns transferResponse

        val dispatcher = StandardTestDispatcher(testScheduler)
        val subject =
            TransferRoundupAmountUseCase(accountsRepository, savingsGoalsRepository, dispatcher)

        subject.execute("saving-goal-uid", BigDecimal.TEN).test {
            awaitItem() shouldBe transferResponse
            awaitComplete()
        }
    }
}