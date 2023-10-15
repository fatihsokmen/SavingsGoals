package com.github.fatihsokmen.savingsgoals.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AmountDto(
    val currency: String,
    val minorUnits: Long
)