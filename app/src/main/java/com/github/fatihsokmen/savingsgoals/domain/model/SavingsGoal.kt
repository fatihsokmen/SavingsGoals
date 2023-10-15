package com.github.fatihsokmen.savingsgoals.domain.model

import java.math.BigDecimal

data class SavingsGoal(
    val savingsGoalUid: String,
    val name: String,
    val totalSaved: TotalSaved
)

data class TotalSaved(
    val currency: String,
    val minorUnits: BigDecimal
)