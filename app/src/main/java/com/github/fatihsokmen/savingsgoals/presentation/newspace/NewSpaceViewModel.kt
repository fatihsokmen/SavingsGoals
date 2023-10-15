package com.github.fatihsokmen.savingsgoals.presentation.newspace

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.fatihsokmen.savingsgoals.domain.CreateSavingsGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class NewSpaceViewModel @Inject constructor(
    private val createSavingsGoalUseCase: CreateSavingsGoalUseCase
) : ViewModel() {

    val uiState = mutableStateOf<NewSpaceState>(NewSpaceState.Lunch)

    fun onCreateNewSpace(name: String) {
        createSavingsGoalUseCase.execute(name)
            .onStart {
                uiState.value = NewSpaceState.Loading
            }
            .onCompletion {
                uiState.value = NewSpaceState.Success
            }.catch {
                uiState.value = NewSpaceState.Error(it.message.orEmpty())
            }
            .launchIn(viewModelScope)
    }
}

sealed interface NewSpaceState {
    object Loading : NewSpaceState
    object Lunch : NewSpaceState
    object Success : NewSpaceState
    data class Error(
        val message: String
    ) : NewSpaceState
}
