package com.github.fatihsokmen.savingsgoals.domain

import app.cash.turbine.test
import com.github.fatihsokmen.savingsgoals.data.AccountsRepository
import com.github.fatihsokmen.savingsgoals.data.SavingsGoalsRepository
import com.github.fatihsokmen.savingsgoals.data.model.AccountDto
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CreateSavingsGoalUseCaseTest {
    private val accountsRepository: AccountsRepository = mockk(relaxed = true)
    private val savingsGoalsRepository: SavingsGoalsRepository = mockk(relaxed = true)

    @Test
    fun `GIVEN use case WHEN executed THEN should create a goal with given name`() = runTest {
        val account = AccountDto("account-uid", "account-category")
        every {
            accountsRepository.getAccounts()
        } returns flowOf(listOf(account))

        coEvery {
            savingsGoalsRepository.createSavingsGoal("account-uid", "goal-name")
        } returns "goal-uid"

        val dispatcher = StandardTestDispatcher(testScheduler)
        val subject =
            CreateSavingsGoalUseCase(accountsRepository, savingsGoalsRepository, dispatcher)

        subject.execute("goal-name").test {
            awaitItem() shouldBe "goal-uid"
            awaitComplete()
        }
    }
}