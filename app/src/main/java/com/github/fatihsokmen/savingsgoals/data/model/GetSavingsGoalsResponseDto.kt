package com.github.fatihsokmen.savingsgoals.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GetSavingsGoalsResponseDto(
    val savingsGoalList: List<SavingsGoalDto>
)