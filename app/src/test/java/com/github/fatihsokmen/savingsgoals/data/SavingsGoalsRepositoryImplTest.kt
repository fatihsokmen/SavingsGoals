package com.github.fatihsokmen.savingsgoals.data

import app.cash.turbine.test
import com.github.fatihsokmen.savingsgoals.data.model.AccountDto
import com.github.fatihsokmen.savingsgoals.data.model.AddIntoSavingsGoalRequestDto
import com.github.fatihsokmen.savingsgoals.data.model.AmountDto
import com.github.fatihsokmen.savingsgoals.data.model.CreateSavingsGoalRequestDto
import com.github.fatihsokmen.savingsgoals.data.model.CreateSavingsGoalResponseDto
import com.github.fatihsokmen.savingsgoals.data.model.GetAccountsResponseDto
import com.github.fatihsokmen.savingsgoals.data.model.GetSavingsGoalsResponseDto
import com.github.fatihsokmen.savingsgoals.data.model.SavingsGoalDto
import com.github.fatihsokmen.savingsgoals.data.model.TotalSavedDto
import com.github.fatihsokmen.savingsgoals.data.model.TransferIntoGoalResponseDto
import io.kotest.matchers.collections.shouldBeSameSizeAs
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.math.BigDecimal

class SavingsGoalsRepositoryImplTest {

    private val savingsGoalsApiService: SavingsGoalsApiService = mockk()

    @Test
    fun `GIVEN goals repository WHEN get saving goals call made THEN it should fetch from api service`() =
        runTest {
            val savingGoalsResponse =
                GetSavingsGoalsResponseDto(
                    listOf(
                        SavingsGoalDto(
                            "saving-goal-uid",
                            "name",
                            TotalSavedDto("GBP", 100.0)
                        )
                    )
                )
            coEvery { savingsGoalsApiService.getSavingsGoals("account-uid") } returns savingGoalsResponse
            val subject = SavingsGoalsRepositoryImpl(savingsGoalsApiService)

            subject.getSavingsGoals("account-uid").test {
                awaitItem() shouldBeSameSizeAs listOf(savingGoalsResponse)
                awaitComplete()
            }
        }

    @Test
    fun `GIVEN goals repository WHEN create saving goals call made THEN it should call api service`() =
        runTest {
            val savingGoalsResponse =
                GetSavingsGoalsResponseDto(
                    listOf(
                        SavingsGoalDto(
                            "saving-goal-uid",
                            "name",
                            TotalSavedDto("GBP", 100.0)
                        )
                    )
                )

            val savingGoalName = "saving-goal-name"
            coEvery {
                savingsGoalsApiService.createSavingsGoal(
                    "account-uid",
                    CreateSavingsGoalRequestDto(savingGoalName, "GBP")
                )
            } returns CreateSavingsGoalResponseDto("saving-goal-uid")

            val subject = SavingsGoalsRepositoryImpl(savingsGoalsApiService)

            val response = subject.createSavingsGoal("account-uid", savingGoalName)

            response shouldBe "saving-goal-uid"
        }

    @Test
    fun `GIVEN goals repository WHEN add into saving goals call made THEN it should call api service`() =
        runTest {
            coEvery {
                savingsGoalsApiService.addIntoSavingsGoal(
                    accountUid = "account-uid",
                    savingsGoalUid = "saving-goal-uid",
                    transferUid = "transfer-uid",
                    AddIntoSavingsGoalRequestDto(AmountDto("GBP", 1000))
                )
            } returns TransferIntoGoalResponseDto(success = true)

            val subject = SavingsGoalsRepositoryImpl(savingsGoalsApiService)

            val response = subject.addIntoSavingsGoal(
                accountUid = "account-uid",
                savingsGoalUid = "saving-goal-uid",
                transferUid = "transfer-uid",
                amount = BigDecimal.TEN
            )

            response.success shouldBe true
        }
}