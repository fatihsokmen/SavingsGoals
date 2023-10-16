package com.github.fatihsokmen.savingsgoals.presentation.home

import app.cash.turbine.test
import com.github.fatihsokmen.savingsgoals.domain.CalculateRoundupUseCase
import com.github.fatihsokmen.savingsgoals.domain.GetSavingsGoalsUseCase
import com.github.fatihsokmen.savingsgoals.domain.TransferRoundupAmountUseCase
import com.github.fatihsokmen.savingsgoals.domain.model.SavingsGoal
import com.github.fatihsokmen.savingsgoals.domain.model.TotalSaved
import com.github.fatihsokmen.savingsgoals.presentation.MainDispatcherRule
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

/**
 * HomeViewModel state tests
 */
class HomeViewModelTest {
    @get: Rule
    val rule = MainDispatcherRule()

    private val getSavingsGoalsUseCase: GetSavingsGoalsUseCase = mockk(relaxed = true)
    private val calculateRoundupUseCase: CalculateRoundupUseCase = mockk(relaxed = true)
    private val transferRoundupUseCase: TransferRoundupAmountUseCase = mockk(relaxed = true)

    @Test
    fun `GIVEN saving goals WHEN screen launched THEN loading and success states should be produces`() =
        runTest {
            val subject = HomeViewModel(
                getSavingsGoalsUseCase,
                calculateRoundupUseCase,
                transferRoundupUseCase
            )

            // GIVEN
            val goal = givenSavingGoals("saving-goal-uid", BigDecimal.TEN)

            // WHEN
            subject.uiState.test {

                // THEN
                with(awaitItem()) {
                    goalState shouldBe GoalsState.Loading
                }

                // THEN
                with(awaitItem()) {
                    goalState.shouldBeInstanceOf<GoalsState.Success>()
                }

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN saving goals WHEN date selected THEN should calculate round up accordingly`() =
        runTest {
            val subject = HomeViewModel(
                getSavingsGoalsUseCase,
                calculateRoundupUseCase,
                transferRoundupUseCase
            )

            // GIVEN
            givenSavingGoals("saving-goal-uid", BigDecimal.TEN)

            subject.uiState.test {
                skipItems(2)

                // WHEN
                val expectedRoundUp = BigDecimal.TEN
                onDateSelected(subject, expectedRoundUp)

                // THEN
                with(awaitItem()) {
                    roundupState.shouldBeInstanceOf<RoundupState.Success>()
                    (roundupState as RoundupState.Success).roundup shouldBe BigDecimal.TEN
                }

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN saving goals and date WHEN transfer executed THEN it should be added into existing goal balance`() =
        runTest {
            val subject = HomeViewModel(
                getSavingsGoalsUseCase,
                calculateRoundupUseCase,
                transferRoundupUseCase
            )

            // GIVEN
            givenSavingGoals("saving-goal-uid", BigDecimal(111))
            onDateSelected(subject, BigDecimal(111))

            subject.uiState.test {
                skipItems(2)

                // WHEN
                onTransferClicked(subject, "saving-goal-uid", BigDecimal(111))

                // THEN
                with(awaitItem()) {
                    goalState.shouldBeInstanceOf<GoalsState.Success>()
                    (goalState as GoalsState.Success).goals.first().totalSaved.minorUnits shouldBe BigDecimal(
                        111
                    )
                }

                cancelAndConsumeRemainingEvents()
            }
        }

    private fun givenSavingGoals(goalUid: String, totalSaved: BigDecimal) = listOf(
        SavingsGoal(
            savingsGoalUid = goalUid,
            name = "goal-name",
            totalSaved = TotalSaved(
                "GBP", totalSaved
            )
        )
    ).also {
        every {
            getSavingsGoalsUseCase.execute()
        } returns flowOf(it)
    }

    private fun onDateSelected(viewModel: HomeViewModel, expectedRoundUp: BigDecimal) {
        every {
            calculateRoundupUseCase.execute(any(), any())
        } returns flowOf(expectedRoundUp)

        viewModel.onDateSelected(2023, 10, 16)
    }

    private fun onTransferClicked(viewModel: HomeViewModel, goalUid: String, amount: BigDecimal) {
        every {
            transferRoundupUseCase.execute(goalUid, amount)
        } returns flowOf(true)

        viewModel.onTransfer()
    }

}