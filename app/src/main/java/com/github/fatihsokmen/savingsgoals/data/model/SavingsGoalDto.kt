package com.github.fatihsokmen.savingsgoals.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SavingsGoalDto(
    val savingsGoalUid: String,
    val name: String,
    val totalSaved: TotalSavedDto
)

@Serializable
data class TotalSavedDto(
    val currency: String,
    val minorUnits: Long
)