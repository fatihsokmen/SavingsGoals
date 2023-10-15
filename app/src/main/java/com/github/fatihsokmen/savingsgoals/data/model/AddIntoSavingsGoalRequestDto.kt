package com.github.fatihsokmen.savingsgoals.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AddIntoSavingsGoalRequestDto(
    val amount: AmountDto
)