package com.github.fatihsokmen.savingsgoals.presentation.newspace

import com.github.fatihsokmen.savingsgoals.domain.CreateSavingsGoalUseCase
import com.github.fatihsokmen.savingsgoals.presentation.MainDispatcherRule
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

/**
 * NewSpaceViewModel state tests
 */
@OptIn(ExperimentalCoroutinesApi::class)
class NewSpaceViewModelTest {
    @get: Rule
    val rule = MainDispatcherRule()

    private val createSavingsGoalUseCase: CreateSavingsGoalUseCase = mockk()

    @Test
    fun `GIVEN view model WHEN screen is launched THEN initial state should be obtained`() =
        runTest {
            // GIVEN - WHEN
            val subject = NewSpaceViewModel(createSavingsGoalUseCase)

            // THEN
            subject.uiState.value.shouldBeInstanceOf<NewSpaceState.Launch>()
        }

    @Test
    fun `GIVEN view model WHEN screen is launched THEN initial state should be obtained 2`() =
        runTest {
            // GIVEN
            val subject = NewSpaceViewModel(createSavingsGoalUseCase)

            // WHEN
            onCreateNewSpace(subject, "space-name", "space-uid")

            // THEN
            advanceUntilIdle()
            subject.uiState.value.shouldBeInstanceOf<NewSpaceState.Success>()
        }

    private fun onCreateNewSpace(subject: NewSpaceViewModel, name: String, spaceUid: String) {
        every { createSavingsGoalUseCase.execute(name) } returns flowOf(spaceUid)
        subject.onCreateNewSpace(name)
    }
}