package com.github.fatihsokmen.savingsgoals.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TransferIntoGoalResponseDto(
    val success: Boolean
)