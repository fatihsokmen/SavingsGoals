package com.github.fatihsokmen.savingsgoals.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateSavingsGoalResponseDto(
    val savingsGoalUid: String
)