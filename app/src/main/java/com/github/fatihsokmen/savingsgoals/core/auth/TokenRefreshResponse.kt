package com.github.fatihsokmen.savingsgoals.core.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenRefreshResponse(
    @SerialName("access_token")
    val accessToken: String
)