package com.github.fatihsokmen.savingsgoals.core.auth

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TokenRefreshService {

    @FormUrlEncoded
    @POST("oauth/access-token")
    suspend fun refreshAccessToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("client_id") clientId: String = "YoFRvHkyAJfPvv6u9eBe",
        @Field("client_secret") clientSecret: String = "v3undZhSkAHkNMeWWZ2OkfbASVjvxEvJmEdUZgfT",
        @Field("refresh_token") refreshToken: String = "ajwx9gORm1VfYqPQqsVDUU1OgeUUKdmp2ibj6kepvBqtw9WIzaBLN60sQPPavS6d",
    ): TokenRefreshResponse
}