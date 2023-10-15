package com.github.fatihsokmen.savingsgoals.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateSavingsGoalRequestDto(
    val name: String,
    val currency: String
)