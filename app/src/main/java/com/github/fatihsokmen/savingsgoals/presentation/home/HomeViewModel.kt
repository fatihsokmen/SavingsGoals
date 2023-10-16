package com.github.fatihsokmen.savingsgoals.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.fatihsokmen.savingsgoals.domain.CalculateRoundupUseCase
import com.github.fatihsokmen.savingsgoals.domain.GetSavingsGoalsUseCase
import com.github.fatihsokmen.savingsgoals.domain.TransferRoundupAmountUseCase
import com.github.fatihsokmen.savingsgoals.domain.model.SavingsGoal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    getSavingsGoalsUseCase: GetSavingsGoalsUseCase,
    calculateRoundupUseCase: CalculateRoundupUseCase,
    private val transferRoundupUseCase: TransferRoundupAmountUseCase
) : ViewModel() {

    private val refreshGoalState = MutableStateFlow(false)
    private val goalsState = refreshGoalState
        .flatMapLatest { getSavingsGoalsUseCase.execute() }
        .map<List<SavingsGoal>, GoalsState>(GoalsState::Success)
        .catch { emit(GoalsState.Error(it.message.orEmpty())) }

    private val dateState = MutableStateFlow(
        Instant.Companion.DISTANT_FUTURE
    )
    private val roundupState = dateState
        .filter { it != Instant.Companion.DISTANT_FUTURE }
        .flatMapLatest { calculateRoundupUseCase.execute(it.minus(7.days), it) }
        .map<BigDecimal, RoundupState>(RoundupState::Success)
        .onStart { emit(RoundupState.Idle) }
        .catch { emit(RoundupState.Error(it.message.orEmpty())) }

    // Composite Screen state for [HomeScreen]
    val uiState = combine(goalsState, roundupState) { goalState, roundupState ->
        HomeViewState(goalState, roundupState)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeViewState(GoalsState.Loading, RoundupState.Idle)
        )

    fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        viewModelScope.launch {
            val localDate = LocalDateTime(year, month + 1, dayOfMonth, 0, 0, 0)
            dateState.emit(localDate.toInstant(TimeZone.UTC))
        }
    }

    fun onTransfer() {
        if (uiState.value.goalState is GoalsState.Success) {
            val savingGoal = (uiState.value.goalState as GoalsState.Success).goals.first()
            val amount = (uiState.value.roundupState as RoundupState.Success).roundup
            viewModelScope.launch {
                transferRoundupUseCase
                    .execute(savingGoal.savingsGoalUid, amount)
                    .map { refreshGoalState.value = refreshGoalState.value.not() }
                    .single()
            }
        }
    }
}

sealed interface GoalsState {
    object Loading : GoalsState
    data class Error(
        val message: String
    ) : GoalsState

    data class Success(
        val goals: List<SavingsGoal>,
    ) : GoalsState
}

sealed interface RoundupState {
    object Idle : RoundupState
    object Loading : RoundupState
    data class Error(
        val message: String
    ) : RoundupState

    data class Success(
        val roundup: BigDecimal
    ) : RoundupState
}

data class HomeViewState(
    val goalState: GoalsState,
    val roundupState: RoundupState
)
