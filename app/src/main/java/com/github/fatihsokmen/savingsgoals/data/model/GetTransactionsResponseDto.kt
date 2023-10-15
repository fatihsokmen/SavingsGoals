package com.github.fatihsokmen.savingsgoals.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GetTransactionsResponseDto(
    val feedItems: List<TransactionDto>
)