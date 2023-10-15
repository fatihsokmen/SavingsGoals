package com.github.fatihsokmen.savingsgoals.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GetAccountsResponseDto(
    val accounts: List<AccountDto>
)