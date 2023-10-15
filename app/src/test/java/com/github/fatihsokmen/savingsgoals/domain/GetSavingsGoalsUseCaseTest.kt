package com.github.fatihsokmen.savingsgoals.domain

import app.cash.turbine.test
import com.github.fatihsokmen.savingsgoals.data.AccountsRepository
import com.github.fatihsokmen.savingsgoals.data.SavingsGoalsRepository
import com.github.fatihsokmen.savingsgoals.data.model.AccountDto
import com.github.fatihsokmen.savingsgoals.data.model.SavingsGoalDto
import com.github.fatihsokmen.savingsgoals.data.model.TotalSavedDto
import com.github.fatihsokmen.savingsgoals.domain.model.SavingsGoal
import com.github.fatihsokmen.savingsgoals.domain.model.TotalSaved
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.math.BigDecimal

class GetSavingsGoalsUseCaseTest {
    private val accountsRepository: AccountsRepository = mockk(relaxed = true)
    private val savingsGoalsRepository: SavingsGoalsRepository = mockk(relaxed = true)

    @Test
    fun `GIVEN use case WHEN executed THEN should return saving goal`() = runTest {
        val account = AccountDto("account-uid", "account-category")
        every {
            accountsRepository.getAccounts()
        } returns flowOf(listOf(account))

        val savingGoal = SavingsGoalDto(
            savingsGoalUid = "saving-goal-uid",
            name = "My Saving Goal",
            totalSaved = TotalSavedDto(
                "GBP", 100.0
            )
        )
        coEvery {
            savingsGoalsRepository.getSavingsGoals("account-uid")
        } returns flowOf(listOf(savingGoal))

        val dispatcher = StandardTestDispatcher(testScheduler)
        val subject =
            GetSavingsGoalsUseCase(accountsRepository, savingsGoalsRepository, dispatcher)

        subject.execute().test {
            awaitItem().first() shouldBe SavingsGoal(
                savingsGoalUid = "saving-goal-uid",
                name = "My Saving Goal",
                totalSaved = TotalSaved(
                    "GBP", BigDecimal(1)
                )
            )

            awaitComplete()
        }
    }
}