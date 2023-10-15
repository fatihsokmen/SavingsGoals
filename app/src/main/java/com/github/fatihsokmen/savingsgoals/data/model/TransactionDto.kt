package com.github.fatihsokmen.savingsgoals.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val feedItemUid: String,
    val amount: AmountDto
)