package com.github.fatihsokmen.savingsgoals.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountDto(
    val accountUid: String,
    val defaultCategory: String
)